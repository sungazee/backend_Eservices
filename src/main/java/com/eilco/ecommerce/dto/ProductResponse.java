package com.eilco.ecommerce.dto;

import com.eilco.ecommerce.model.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {
    Product product;
}
