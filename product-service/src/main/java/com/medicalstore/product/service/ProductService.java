package com.medicalstore.product.service;

import com.medicalstore.product.dto.ProductRequest;
import com.medicalstore.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    // Add Medicine
    ProductResponse createProduct(ProductRequest request);

    // Update Medicine
    ProductResponse updateProduct(Long id, ProductRequest request);

    // Delete Medicine
    void deleteProduct(Long id);

    // Get Medicine Details
    ProductResponse getProductById(Long id);

    // Get All Medicines
    List<ProductResponse> getAllProducts();

    // Search Medicine by Name
    List<ProductResponse> searchMedicine(String medicineName);
}