package com.medicalstore.order.repository;

import com.medicalstore.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by customer name
     */
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);

    /**
     * Find orders by customer mobile
     */
    List<Order> findByCustomerMobile(String customerMobile);

    /**
     * Find orders by status
     */
    List<Order> findByStatus(String status);

}