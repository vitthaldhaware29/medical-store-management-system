package com.medicalstore.inventory.consumer;

import com.medicalstore.inventory.dto.ProductEvent;
import com.medicalstore.inventory.dto.InventoryRequest;
import com.medicalstore.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "medicine-created",
            groupId = "inventory-group"
    )
    public void consume(ProductEvent event) {

        log.info("Received Product Event : {}", event);

        InventoryRequest request = InventoryRequest.builder()
                .productId(event.getProductId())
                .medicineName(event.getMedicineName())
                .batchNumber(event.getBatchNumber())
                .quantity(0)          // Initial stock
                .minimumStock(20)     // Default minimum stock
                .build();

        inventoryService.createInventory(request);

        log.info("Inventory Created Successfully");
    }

}