package com.medicalstore.notification.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEvent {

    private Long productId;

    private String medicineName;

    private String manufacturer;

    private String batchNumber;

    private LocalDate expiryDate;

    private BigDecimal price;

    private String category;

    private String eventType;

    private LocalDateTime eventTime;

}