package com.eilco.ecommerce.dto;

import java.math.BigDecimal;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal priceAtTime;
    private BigDecimal subTotal;
}
