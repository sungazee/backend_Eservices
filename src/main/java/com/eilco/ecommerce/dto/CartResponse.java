package com.eilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemResponse> cartItems;
    private BigDecimal totalAmount;
}
