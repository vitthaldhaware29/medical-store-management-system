package com.medicalstore.product.producer;

import com.medicalstore.product.dto.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductKafkaProducer {


    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;


    private static final String MEDICINE_CREATED_TOPIC = "medicine-created";

    private static final String MEDICINE_UPDATED_TOPIC = "medicine-updated";

    private static final String MEDICINE_DELETED_TOPIC = "medicine-deleted";


    /**
     * Send Medicine Created Event
     */
    public void sendMedicineCreatedEvent(ProductEvent event) {

        kafkaTemplate.send(
                MEDICINE_CREATED_TOPIC,
                event.getProductId().toString(),
                event
        );

        log.info(
                "Medicine created event sent : {}",
                event
        );
    }


    /**
     * Send Medicine Updated Event
     */
    public void sendMedicineUpdatedEvent(ProductEvent event) {

        kafkaTemplate.send(
                MEDICINE_UPDATED_TOPIC,
                event.getProductId().toString(),
                event
        );

        log.info(
                "Medicine updated event sent : {}",
                event
        );
    }


    /**
     * Send Medicine Deleted Event
     */
    public void sendMedicineDeletedEvent(ProductEvent event) {

        kafkaTemplate.send(
                MEDICINE_DELETED_TOPIC,
                event.getProductId().toString(),
                event
        );

        log.info(
                "Medicine deleted event sent : {}",
                event
        );
    }
}