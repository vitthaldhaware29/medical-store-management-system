package com.medicalstore.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {

    private Long productId;

    private String medicineName;

    private Integer quantity;

    private String eventType;

    private LocalDateTime eventTime;

}