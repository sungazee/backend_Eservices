package com.eilco.ecommerce.dto;


import com.eilco.ecommerce.model.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponse {
    Category category;
}