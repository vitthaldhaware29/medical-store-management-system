package com.medicalstore.inventory.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name="inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long productId;


    private String medicineName;


    private String batchNumber;


    private Integer quantity;


    private Integer minimumStock;


    private LocalDateTime createdDate;


    private LocalDateTime updatedDate;



    @PrePersist
    public void createDate(){

        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }


    @PreUpdate
    public void updateDate(){

        updatedDate = LocalDateTime.now();
    }

}