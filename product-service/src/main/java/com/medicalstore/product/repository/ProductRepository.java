package com.medicalstore.product.repository;

import com.medicalstore.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    // Find Medicine by exact name
    Optional<Product> findByMedicineName(String medicineName);


    // Search Medicine by partial name
    List<Product> findByMedicineNameContainingIgnoreCase(String medicineName);


    // Search by Category
    List<Product> findByCategoryIgnoreCase(String category);


    // Search by Manufacturer
    List<Product> findByManufacturerIgnoreCase(String manufacturer);


    // Check duplicate Batch Number
    boolean existsByBatchNumber(String batchNumber);


    // Find medicine by Batch Number
    Optional<Product> findByBatchNumber(String batchNumber);


    // Delete medicine by Batch Number
    void deleteByBatchNumber(String batchNumber);

}