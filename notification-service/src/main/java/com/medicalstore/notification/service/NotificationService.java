package com.medicalstore.notification.service;

import com.medicalstore.notification.dto.ProductEvent;

public interface NotificationService {

    void sendNotification(ProductEvent event);

}