package com.medicalstore.notification.dto;

import lombok.Data;

@Data
public class OrderEvent {
    private String orderId;
    private String productName;
    private int quantity;
    private double price;
    private String status;
}

