package com.eilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subTotal;
}
