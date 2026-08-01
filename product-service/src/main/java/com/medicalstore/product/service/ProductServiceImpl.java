package com.medicalstore.product.service;

import com.medicalstore.product.dto.ProductRequest;
import com.medicalstore.product.dto.ProductResponse;
import com.medicalstore.product.entity.Product;
import com.medicalstore.product.exception.ProductNotFoundException;
import com.medicalstore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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

        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Medicine not found with id : " + id));

        product.setMedicineName(request.getMedicineName());
        product.setManufacturer(request.getManufacturer());
        product.setBatchNumber(request.getBatchNumber());
        product.setExpiryDate(request.getExpiryDate());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Medicine not found with id : " + id));

        productRepository.delete(product);
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