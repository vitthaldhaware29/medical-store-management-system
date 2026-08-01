package com.medicalstore.inventory.consumer;

import com.medicalstore.inventory.dto.InventoryRequest;
import com.medicalstore.inventory.dto.ProductEvent;
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

    @KafkaListener(topics = "medicine-created", groupId = "inventory-group")
    public void consumeProductEvent(ProductEvent event) {
        log.info("Consumed Product Event: {}", event);

        // Create inventory entry for the new product
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productId(event.getProductId())
                .medicineName(event.getMedicineName())
                .batchNumber(event.getBatchNumber())
                .quantity(0) // Default quantity
                .minimumStock(10) // Default minimum stock
                .build();

        inventoryService.createInventory(inventoryRequest);
    }
    @KafkaListener(topics = "medicine-updated", groupId = "inventory-group")
    public void consumeUpdatedProductEvent(ProductEvent event) {
        log.info("Consumed Updated Product Event: {}", event);

        // Update inventory entry for the updated product
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productId(event.getProductId())
                .medicineName(event.getMedicineName())
                .batchNumber(event.getBatchNumber())
                .build();

        inventoryService.updateInventory(inventoryRequest);
    }
    @KafkaListener(topics = "medicine-deleted", groupId = "inventory-group")
    public void consumeDeletedProductEvent(ProductEvent event) {
        log.info("Consumed Deleted Product Event: {}", event);

        // Delete inventory entry for the deleted product
        inventoryService.deleteInventory(event.getProductId());
    }
}