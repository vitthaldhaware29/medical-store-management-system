package com.medicalstore.order.service;

import com.medicalstore.order.client.InventoryClient;
import com.medicalstore.order.dto.InventoryResponse;
import com.medicalstore.order.dto.OrderItemRequest;
import com.medicalstore.order.dto.OrderRequest;
import com.medicalstore.order.dto.OrderResponse;
import com.medicalstore.order.entity.Order;
import com.medicalstore.order.entity.OrderItem;
import com.medicalstore.order.exception.OrderNotFoundException;
import com.medicalstore.order.producer.OrderKafkaProducer;
import com.medicalstore.order.repository.OrderItemRepository;
import com.medicalstore.order.repository.OrderRepository;
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

            InventoryResponse inventory =
                    inventoryClient.getInventory(item.getMedicineId());

            if (inventory == null) {
                throw new RuntimeException("Medicine not found.");
            }

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for " + inventory.getMedicineName());
            }
        }

        // Calculate Total Amount
        BigDecimal totalAmount = request.getItems()
                .stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Save Order
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .customerMobile(request.getCustomerMobile())
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .build();

        Order savedOrder = orderRepository.save(order);

        // Save Order Items
        for (OrderItemRequest item : request.getItems()) {

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .medicineId(item.getMedicineId())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();

            orderItemRepository.save(orderItem);

            // Reduce Stock
            inventoryClient.reduceStock(
                    item.getMedicineId(),
                    item.getQuantity());
        }

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId,
                                     OrderRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id : " + orderId));

        order.setCustomerName(request.getCustomerName());
        order.setCustomerMobile(request.getCustomerMobile());

        Order updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id : " + orderId));

        order.setStatus("CANCELLED");

        orderRepository.save(order);

        // Restore Inventory
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {

            inventoryClient.increaseStock(
                    item.getMedicineId(),
                    item.getQuantity());

        }

    }


    @Override
    public InventoryResponse verifyStock(OrderRequest request) {
        for (OrderItemRequest item : request.getItems()) {

            InventoryResponse inventory =
                    inventoryClient.getInventory(item.getMedicineId());

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
        }

        return null;
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
    public OrderResponse generateInvoice(Long orderId) {
        return null;
    }

    @Override
    public InventoryResponse verifyStock(Long medicineId) {
        return null;
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