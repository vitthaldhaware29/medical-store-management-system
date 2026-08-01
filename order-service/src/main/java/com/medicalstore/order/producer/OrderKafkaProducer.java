package com.medicalstore.order.producer;

import com.medicalstore.order.config.KafkaTopicConfig;
import com.medicalstore.order.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    /**
     * Publish Order Created Event
     */
    public void sendOrderCreatedEvent(OrderEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CREATED_TOPIC,
                event);

        log.info("Order Created Event Published : {}", event);
    }

    /**
     * Publish Order Updated Event
     */
    public void sendOrderUpdatedEvent(OrderEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_UPDATED_TOPIC,
                event);

        log.info("Order Updated Event Published : {}", event);
    }

    /**
     * Publish Order Cancelled Event
     */
    public void sendOrderCancelledEvent(OrderEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CANCELLED_TOPIC,
                event);

        log.info("Order Cancelled Event Published : {}", event);
    }

    /**
     * Publish Invoice Generated Event
     */
    public void sendInvoiceGeneratedEvent(OrderEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.INVOICE_GENERATED_TOPIC,
                event);

        log.info("Invoice Generated Event Published : {}", event);
    }
}