package com.medicalstore.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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