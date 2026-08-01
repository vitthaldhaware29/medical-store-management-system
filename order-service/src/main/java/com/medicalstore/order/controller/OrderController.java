package com.medicalstore.order.controller;

import com.medicalstore.order.dto.InventoryResponse;
import com.medicalstore.order.dto.OrderRequest;
import com.medicalstore.order.dto.OrderResponse;
import com.medicalstore.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create Order
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update Order
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response =
                orderService.updateOrder(orderId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancel Order
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable("orderId") Long orderId) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok("Order cancelled successfully.");
    }

    /**
     * Get Order By Id
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("orderId") Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId));
    }

    /**
     * Get All Orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders());
    }

    /**
     * Generate Invoice
     */
    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<byte[]> generateInvoice(
            @PathVariable("orderId") Long orderId) {

        return orderService.generateInvoice(orderId);
    }

    /**
     * Verify Stock
     */
    @GetMapping("/verify-stock/{medicineId}")
    public ResponseEntity<InventoryResponse> verifyStock(
            @PathVariable("medicineId") Long medicineId) {

        return ResponseEntity.ok(
                orderService.verifyStock(medicineId));
    }

}