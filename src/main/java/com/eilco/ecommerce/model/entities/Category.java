package com.eilco.ecommerce.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(length=500)
    private String description;

    @Column(length=500)
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_cat_id", referencedColumnName = "id")
    private List<Product> products;

}