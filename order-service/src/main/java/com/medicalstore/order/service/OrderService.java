package com.medicalstore.order.service;

import com.medicalstore.order.dto.InventoryResponse;
import com.medicalstore.order.dto.OrderResponse;
import com.medicalstore.order.dto.OrderRequest;

import java.util.List;

public interface OrderService {

    /**
     * Create a new order
     */
    OrderResponse createOrder(OrderRequest request);

    /**
     * Update an existing order
     */
    OrderResponse updateOrder(Long orderId, OrderRequest request);

    /**
     * Cancel an order
     */
    void cancelOrder(Long orderId);

    InventoryResponse verifyStock(OrderRequest request);

    /**
     * Get order by id
     */
    OrderResponse getOrderById(Long orderId);

    /**
     * Get all orders
     */
    List<OrderResponse> getAllOrders();

    /**
     * Generate invoice
     */
    OrderResponse generateInvoice(Long orderId);

    /**
     * Verify stock with Inventory Service
     */
    InventoryResponse verifyStock(Long medicineId);

}