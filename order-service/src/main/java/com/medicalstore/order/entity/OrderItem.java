package com.medicalstore.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many OrderItems belong to one Order
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Medicine/Product Id from Product Service
     */
    @Column(nullable = false)
    private Long medicineId;

    /**
     * Quantity Ordered
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Price of one medicine
     */
    @Column(nullable = false)
    private BigDecimal price;

}