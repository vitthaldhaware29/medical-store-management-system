package com.medicalstore.notification.service;

import com.medicalstore.notification.dto.OrderEvent;

public interface NotificationService {

    void sendNotification(OrderEvent event);

}