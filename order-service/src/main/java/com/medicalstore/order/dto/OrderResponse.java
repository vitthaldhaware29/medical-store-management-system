package com.medicalstore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private String customerName;

    private String customerMobile;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createdDate;

    private List<OrderItemRequest> items;

}