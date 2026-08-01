package com.medicalstore.product.controller;

import com.medicalstore.product.dto.ProductRequest;
import com.medicalstore.product.dto.ProductResponse;
import com.medicalstore.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

@Autowired
    private final ProductService productService;


    /**
     * Add Medicine
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.createProduct(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    /**
     * Get Medicine Details By Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable("id") Long id) {

        ProductResponse response =
                productService.getProductById(id);

        return ResponseEntity.ok(response);
    }


    /**
     * Get All Medicines
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }


    /**
     * Search Medicine By Name
     *
     * Example:
     * GET /api/v1/products/search?name=Paracetamol
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchMedicine(
            @RequestParam("name") String name) {


        List<ProductResponse> products =
                productService.searchMedicine(name);


        return ResponseEntity.ok(products);
    }


    /**
     * Update Medicine
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductRequest request) {


        ProductResponse response =
                productService.updateProduct(id, request);


        return ResponseEntity.ok(response);
    }


    /**
     * Delete Medicine
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                "Medicine deleted successfully"
        );
    }
}