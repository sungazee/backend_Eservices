package com.eilco.ecommerce.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="produit")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(length=500)
    private String description;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private int quantity;

    @Column
    private boolean active ;

    @Column(length=500)
    private String imageUrl;
    // Getters et setters
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

}
