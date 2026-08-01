package com.medicalstore.order.repository;

import com.medicalstore.order.entity.Order;
import com.medicalstore.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Get all items for an order
     */
    List<OrderItem> findByOrder(Order order);

    /**
     * Get all items using order id
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Find all orders containing a medicine
     */
    List<OrderItem> findByMedicineId(Long medicineId);

    /**
     * Delete all items for an order
     */
    void deleteByOrderId(Long orderId);

}