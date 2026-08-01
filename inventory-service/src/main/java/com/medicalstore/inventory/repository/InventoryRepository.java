package com.medicalstore.inventory.repository;


import com.medicalstore.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory,Long> {


    Optional<Inventory> findByProductId(Long productId);


    List<Inventory> findByQuantityLessThan(
            Integer minimumStock
    );

}