package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.PaymentRequest;
import com.eilco.ecommerce.model.entities.Cart;
import com.eilco.ecommerce.model.entities.PaymentStatus;
import com.eilco.ecommerce.service.CartService;
import com.eilco.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService; // Service pour accéder au panier

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        // Vérifier que le panier existe
        Cart cart = cartService.findById(paymentRequest.getCartId());
        if (cart == null) {
            return ResponseEntity.badRequest().body("Le panier n'existe pas.");
        }

        // Appeler la méthode de création de session en passant le panier
        String paymentUrl = paymentService.createPaymentSession(paymentRequest, cart);
        if (paymentUrl.startsWith("Stripe error: ")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(paymentUrl);
        }
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @PostMapping("/update-status")
    public ResponseEntity<String> updatePaymentStatus(@RequestParam Long paymentId, @RequestParam PaymentStatus status) {
        try {
            paymentService.updatePaymentStatus(paymentId, status);
            return ResponseEntity.ok("Statut de paiement mis à jour");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du statut");
        }
    }
}
