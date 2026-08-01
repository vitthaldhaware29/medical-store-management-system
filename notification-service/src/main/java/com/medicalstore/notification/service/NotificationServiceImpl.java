package com.medicalstore.notification.service;

import com.medicalstore.notification.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(OrderEvent event) {
        log.info("========================================");
        log.info("Notification Received");
        log.info("========================================");

        switch (event.getStatus()) {
            case "SUCCESS":
                handleOrderSuccess(event);
                break;
            case "CANCELLED":
                handleOrderCancel(event);
                break;
            case "UPDATED":
                handleOrderUpdate(event);
                break;
            default:
                log.warn("Unknown Order Status: {}", event.getStatus());
        }

        log.info("========================================");
    }

    private void handleOrderSuccess(OrderEvent event) {
        log.info("Order Success Notification");
        log.info("Order ID      : {}", event.getOrderId());
        log.info("Product Name  : {}", event.getProductName());
        log.info("Quantity      : {}", event.getQuantity());
        log.info("Price         : {}", event.getPrice());
        log.info("Message       : Order placed successfully!");
    }

    private void handleOrderCancel(OrderEvent event) {
        log.info("Order Cancel Notification");
        log.info("Order ID      : {}", event.getOrderId());
        log.info("Product Name  : {}", event.getProductName());
        log.info("Quantity      : {}", event.getQuantity());
        log.info("Price         : {}", event.getPrice());
        log.info("Message       : Order has been cancelled.");
    }

    private void handleOrderUpdate(OrderEvent event) {
        log.info("Order Update Notification");
        log.info("Order ID      : {}", event.getOrderId());
        log.info("Product Name  : {}", event.getProductName());
        log.info("Quantity      : {}", event.getQuantity());
        log.info("Price         : {}", event.getPrice());
        log.info("Message       : Order details have been updated.");
    }

    // Future Implementation:
    // Send Email
    // Send SMS
    // Send Push Notification
}