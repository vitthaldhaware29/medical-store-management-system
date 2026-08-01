package com.medicalstore.notification.consumer;

import com.medicalstore.notification.dto.OrderEvent;
import com.medicalstore.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-event", groupId = "notification-group")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Kafka Order Event Received: {}", event);
        notificationService.sendNotification(event);
    }
}