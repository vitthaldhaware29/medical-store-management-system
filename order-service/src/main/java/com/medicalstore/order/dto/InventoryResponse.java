package com.medicalstore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long id;

    private Long productId;

    private String medicineName;

    private String batchNumber;

    private Integer quantity;

    private Integer minimumStock;

}