package com.medicalstore.order.producer;

import com.medicalstore.order.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String TOPIC = "order-event";

    public void publishOrderEvent(OrderEvent event) {
        log.info("Publishing Order Event: {}", event);
        kafkaTemplate.send(TOPIC, event);
        log.info("Order Event Published Successfully to Topic: {}", TOPIC);
    }
}