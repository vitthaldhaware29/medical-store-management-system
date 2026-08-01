package com.medicalstore.inventory.service;


import com.medicalstore.inventory.dto.InventoryRequest;
import com.medicalstore.inventory.dto.InventoryResponse;

import java.util.List;


public interface InventoryService {


    // Maintain Stock
    InventoryResponse createInventory(
            InventoryRequest request);



    // Increase Stock
    InventoryResponse increaseStock(
            Long productId,
            Integer quantity);



    // Reduce Stock
    InventoryResponse reduceStock(
            Long productId,
            Integer quantity);



    // Get Inventory
    InventoryResponse getInventory(
            Long productId);



    // Low Stock Alert
    List<InventoryResponse> getLowStockProducts();

}