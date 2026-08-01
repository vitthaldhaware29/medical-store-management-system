package com.medicalstore.inventory.dto;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {


    private Long id;

    private Long productId;

    private String medicineName;

    private String batchNumber;

    private Integer quantity;

    private Integer minimumStock;

}