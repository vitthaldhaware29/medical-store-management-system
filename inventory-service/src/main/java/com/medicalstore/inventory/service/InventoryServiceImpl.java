package com.medicalstore.inventory.service;


import com.medicalstore.inventory.dto.InventoryRequest;
import com.medicalstore.inventory.dto.InventoryResponse;
import com.medicalstore.inventory.entity.Inventory;
import com.medicalstore.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl
        implements InventoryService {


    private final InventoryRepository repository;



    @Override
    public InventoryResponse createInventory(
            InventoryRequest request) {


        Inventory inventory =
                Inventory.builder()

                        .productId(request.getProductId())

                        .medicineName(request.getMedicineName())

                        .batchNumber(request.getBatchNumber())

                        .quantity(request.getQuantity())

                        .minimumStock(request.getMinimumStock())

                        .build();


        return map(
                repository.save(inventory)
        );

    }



    @Override
    public InventoryResponse increaseStock(
            Long productId,
            Integer quantity) {


        Inventory inventory =
                repository.findByProductId(productId)
                        .orElseThrow();


        inventory.setQuantity(
                inventory.getQuantity()+quantity
        );


        return map(
                repository.save(inventory)
        );
    }




    @Override
    public InventoryResponse reduceStock(
            Long productId,
            Integer quantity) {


        Inventory inventory =
                repository.findByProductId(productId)
                        .orElseThrow();



        if(inventory.getQuantity() < quantity){

            throw new RuntimeException(
                    "Insufficient stock"
            );
        }


        inventory.setQuantity(
                inventory.getQuantity()-quantity
        );


        return map(
                repository.save(inventory)
        );
    }





    @Override
    public InventoryResponse getInventory(
            Long productId) {


        Inventory inventory =
                repository.findByProductId(productId)
                        .orElseThrow();


        return map(inventory);
    }





    @Override
    public List<InventoryResponse> getLowStockProducts(){


        return repository
                .findByQuantityLessThan(10)
                .stream()
                .map(this::map)
                .toList();

    }




    private InventoryResponse map(
            Inventory inventory){


        return InventoryResponse.builder()

                .id(inventory.getId())

                .productId(inventory.getProductId())

                .medicineName(inventory.getMedicineName())

                .batchNumber(inventory.getBatchNumber())

                .quantity(inventory.getQuantity())

                .minimumStock(inventory.getMinimumStock())

                .build();

    }

}