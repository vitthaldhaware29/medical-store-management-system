package com.medicalstore.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String medicineName;

    private String manufacturer;

    private String batchNumber;

    private LocalDate expiryDate;

    private BigDecimal price;

    private String category;

    private LocalDateTime createdDate;
}