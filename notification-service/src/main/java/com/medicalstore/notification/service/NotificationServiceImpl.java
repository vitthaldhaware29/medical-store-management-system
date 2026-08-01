package com.medicalstore.notification.service;

import com.medicalstore.notification.dto.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(ProductEvent event) {

        log.info("========================================");
        log.info("Notification Received");
        log.info("Medicine Name : {}", event.getMedicineName());
        log.info("Manufacturer  : {}", event.getManufacturer());
        log.info("Batch Number  : {}", event.getBatchNumber());
        log.info("Price         : {}", event.getPrice());
        log.info("Event Type    : {}", event.getEventType());
        log.info("Event Time    : {}", event.getEventTime());
        log.info("========================================");

        // Future Implementation:
        // Send Email
        // Send SMS
        // Send Push Notification

    }
}