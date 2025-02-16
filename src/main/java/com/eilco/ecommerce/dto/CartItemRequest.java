package com.eilco.ecommerce.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
