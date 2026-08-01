package com.medicalstore.order.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.medicalstore.order.client.InventoryClient;
import com.medicalstore.order.dto.*;
import com.medicalstore.order.entity.Order;
import com.medicalstore.order.entity.OrderItem;
import com.medicalstore.order.exception.OrderNotFoundException;
import com.medicalstore.order.producer.OrderKafkaProducer;
import com.medicalstore.order.repository.OrderItemRepository;
import com.medicalstore.order.repository.OrderRepository;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final InventoryClient inventoryClient;

    private final OrderKafkaProducer orderKafkaProducer;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // Verify Stock
        for (OrderItemRequest item : request.getItems()) {
            InventoryResponse inventory = inventoryClient.getInventory(item.getMedicineId());
            if (inventory == null || inventory.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + item.getMedicineId());
            }
        }

        // Calculate Total Amount
        BigDecimal totalAmount = request.getItems()
                .stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Save Order
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .customerMobile(request.getCustomerMobile())
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .build();
        Order savedOrder = orderRepository.save(order);

        // Save Order Items and Reduce Stock
        for (OrderItemRequest item : request.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .medicineId(item.getMedicineId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
            orderItemRepository.save(orderItem);
            inventoryClient.reduceStock(item.getMedicineId(), item.getQuantity());
        }

        // Publish OrderEvent to Kafka
        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(savedOrder.getId())
                .customerName(savedOrder.getCustomerName())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .build();
        orderKafkaProducer.publishOrderEvent(orderEvent);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id : " + orderId));

        order.setCustomerName(request.getCustomerName());
        order.setCustomerMobile(request.getCustomerMobile());
        Order updatedOrder = orderRepository.save(order);

        // Publish OrderEvent
        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(updatedOrder.getId())
                .customerName(updatedOrder.getCustomerName())
                .totalAmount(updatedOrder.getTotalAmount())
                .status(updatedOrder.getStatus())
                .build();
        orderKafkaProducer.publishOrderEvent(orderEvent);

        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id : " + orderId));

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        // Restore Inventory
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            inventoryClient.increaseStock(item.getMedicineId(), item.getQuantity());
        }

        // Publish OrderEvent
        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .build();
        orderKafkaProducer.publishOrderEvent(orderEvent);
    }


    @Override
    public InventoryResponse verifyStock(OrderRequest request) {
        InventoryResponse lastCheckedInventory = null;

        for (OrderItemRequest item : request.getItems()) {
            InventoryResponse inventory = inventoryClient.getInventory(item.getMedicineId());

            if (inventory == null) {
                throw new RuntimeException(
                        "Medicine not found. Id : " + item.getMedicineId());
            }

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for "
                                + inventory.getMedicineName()
                                + ". Available : "
                                + inventory.getQuantity()
                                + ", Requested : "
                                + item.getQuantity());
            }

            lastCheckedInventory = inventory;
        }

        return lastCheckedInventory;
    }
    @Override
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        return mapToResponse(order);

    }
    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public ResponseEntity<byte[]> generateInvoice(Long orderId) {
        // Fetch the order details
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        // Create a PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add content to the PDF
            document.add(new Paragraph("Invoice for Order ID: " + order.getId()));
            document.add(new Paragraph("Customer Name: " + order.getCustomerName()));
            document.add(new Paragraph("Customer Mobile: " + order.getCustomerMobile()));
            document.add(new Paragraph("Total Amount: " + order.getTotalAmount()));
            document.add(new Paragraph("Status: " + order.getStatus()));
            document.add(new Paragraph("Created Date: " + order.getCreatedDate()));

            document.add(new Paragraph("\nOrder Items:"));
            List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
            for (OrderItem item : items) {
                document.add(new Paragraph("- Medicine ID: " + item.getMedicineId() +
                        ", Quantity: " + item.getQuantity() +
                        ", Price: " + item.getPrice()));
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        // Return the PDF as a downloadable file
        byte[] pdfBytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_" + orderId + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Override
    public InventoryResponse verifyStock(Long medicineId) {
        InventoryResponse inventory = inventoryClient.getInventory(medicineId);

        if (inventory == null) {
            throw new RuntimeException("Medicine not found. Id: " + medicineId);
        }

        return inventory;
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerMobile(order.getCustomerMobile())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdDate(order.getCreatedDate())
                .build();
    }
}