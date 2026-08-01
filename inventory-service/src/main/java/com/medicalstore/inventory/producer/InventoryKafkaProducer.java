package com.medicalstore.inventory.producer;

import com.medicalstore.inventory.dto.InventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryKafkaProducer {

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    private static final String LOW_STOCK_TOPIC = "inventory-low-stock";

    public void sendLowStockEvent(InventoryEvent event) {

        kafkaTemplate.send(
                LOW_STOCK_TOPIC,
                event.getProductId().toString(),
                event
        );

        log.info("Low Stock Event Published : {}", event);
    }

}