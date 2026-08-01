package com.medicalstore.product.service;

import com.medicalstore.product.dto.ProductEvent;
import com.medicalstore.product.dto.ProductRequest;
import com.medicalstore.product.dto.ProductResponse;
import com.medicalstore.product.entity.Product;
import com.medicalstore.product.exception.ProductNotFoundException;
import com.medicalstore.product.producer.ProductKafkaProducer;
import com.medicalstore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductKafkaProducer productKafkaProducer;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByBatchNumber(request.getBatchNumber())) {
            throw new RuntimeException("Batch Number already exists.");
        }

        Product product = Product.builder()
                .medicineName(request.getMedicineName())
                .manufacturer(request.getManufacturer())
                .batchNumber(request.getBatchNumber())
                .expiryDate(request.getExpiryDate())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        Product savedProduct = productRepository.save(product);

        // Publish ProductEvent to Kafka
        ProductEvent productEvent = ProductEvent.builder()
                .productId(savedProduct.getId())
                .medicineName(savedProduct.getMedicineName())
                .batchNumber(savedProduct.getBatchNumber())
                .build();
        productKafkaProducer.sendMedicineCreatedEvent(productEvent);

        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Medicine not found with id : " + id));

        boolean isInventoryFieldModified = !product.getBatchNumber().equals(request.getBatchNumber()) ||
                !product.getExpiryDate().equals(request.getExpiryDate());

        product.setMedicineName(request.getMedicineName());
        product.setManufacturer(request.getManufacturer());
        product.setBatchNumber(request.getBatchNumber());
        product.setExpiryDate(request.getExpiryDate());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        Product updatedProduct = productRepository.save(product);

        // Publish ProductEvent only if inventory-related fields are modified
        if (isInventoryFieldModified) {
            ProductEvent productEvent = ProductEvent.builder()
                    .productId(updatedProduct.getId())
                    .medicineName(updatedProduct.getMedicineName())
                    .batchNumber(updatedProduct.getBatchNumber())
                    .build();
            productKafkaProducer.sendMedicineUpdatedEvent(productEvent);
        }

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Medicine not found with id : " + id));

        productRepository.delete(product);

        // Publish ProductEvent for product deletion
        ProductEvent productEvent = ProductEvent.builder()
                .productId(product.getId())
                .medicineName(product.getMedicineName())
                .batchNumber(product.getBatchNumber())
                .build();
        productKafkaProducer.sendMedicineDeletedEvent(productEvent);
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Medicine not found with id : " + id));

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchMedicine(String medicineName) {

        return productRepository
                .findByMedicineName(medicineName)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Product Entity to ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .medicineName(product.getMedicineName())
                .manufacturer(product.getManufacturer())
                .batchNumber(product.getBatchNumber())
                .expiryDate(product.getExpiryDate())
                .price(product.getPrice())
                .category(product.getCategory())
                .createdDate(product.getCreatedDate())
                .build();
    }
}