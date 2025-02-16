package com.eilco.ecommerce.dto;

import com.eilco.ecommerce.model.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentResponse {
    private Long id;
    private Long cartId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}