package com.medicalstore.order.client;

import com.medicalstore.order.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;

    private static final String INVENTORY_BASE_URL =
            "http://localhost:8082/api/v1/inventory";

    /**
     * Verify Stock
     */
    public InventoryResponse getInventory(Long productId) {

        ResponseEntity<InventoryResponse> response =
                restTemplate.getForEntity(
                        INVENTORY_BASE_URL + "/" + productId,
                        InventoryResponse.class);

        return response.getBody();
    }

    /**
     * Reduce Stock
     */
    public void reduceStock(Long productId, Integer quantity) {

        restTemplate.put(
                INVENTORY_BASE_URL + "/"
                        + productId
                        + "/reduce?quantity="
                        + quantity,
                null);
    }

    /**
     * Increase Stock
     */
    public void increaseStock(Long productId, Integer quantity) {

        restTemplate.put(
                INVENTORY_BASE_URL + "/"
                        + productId
                        + "/increase?quantity="
                        + quantity,
                null
        );
    }

}