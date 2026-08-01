package com.medicalstore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private Long orderId;

    private String customerName;

    private String customerMobile;

    private BigDecimal totalAmount;

    private String status;

    private String eventType;

    private LocalDateTime eventTime;

}