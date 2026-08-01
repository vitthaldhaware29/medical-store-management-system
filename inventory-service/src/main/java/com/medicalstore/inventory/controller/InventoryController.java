package com.medicalstore.inventory.controller;


import com.medicalstore.inventory.dto.InventoryRequest;
import com.medicalstore.inventory.dto.InventoryResponse;
import com.medicalstore.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {



    private final InventoryService service;



    @PostMapping
    public InventoryResponse create(
            @RequestBody InventoryRequest request){

        return service.createInventory(request);

    }




    @PutMapping("/{productId}/increase")
    public InventoryResponse increaseStock(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Integer quantity){

        return service.increaseStock(
                productId,
                quantity
        );
    }





    @PutMapping("/{productId}/reduce")
    public InventoryResponse reduceStock(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Integer quantity){

        return service.reduceStock(
                productId,
                quantity
        );
    }





    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable("productId") Long productId){

        return service.getInventory(productId);
    }





    @GetMapping("/low-stock")
    public List<InventoryResponse> lowStock(){

        return service.getLowStockProducts();

    }

}