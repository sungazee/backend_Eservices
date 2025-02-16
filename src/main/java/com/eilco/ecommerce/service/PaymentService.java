package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.PaymentRequest;
import com.eilco.ecommerce.model.entities.Cart;
import com.eilco.ecommerce.model.entities.Payment;
import com.eilco.ecommerce.model.entities.PaymentStatus;
import com.eilco.ecommerce.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private final String stripeApiKey = "sk_test_51QrfG7FaXIYxcjFbUWOsGz8G0zcRQ8qYBouP5IeCIypPrbNqx0Vh9dJvwkIR2KkruTBIHOuvwXnurvnDT0TYmad000KcLiXsbO";

    // Modification de la méthode pour recevoir également le panier (Cart)
    public String createPaymentSession(PaymentRequest paymentRequest, Cart cart) {
        try {
            Payment payment = new Payment();
            payment.setAmount(paymentRequest.getAmount());
            payment.setStatus(PaymentStatus.PENDING);
            LocalDateTime currentDate = LocalDateTime.now();
            payment.setPaymentDate(currentDate);

            // Associer le paiement au panier existant via le champ "cart"
            payment.setCart(cart);
            paymentRepository.save(payment);

            Stripe.apiKey = stripeApiKey;
            Session session = Session.create(
                    Map.of(
                            "payment_method_types", List.of("card"),
                            "line_items", List.of(
                                    Map.of(
                                            "price_data", Map.of(
                                                    "currency", "eur",
                                                    "product_data", Map.of("name", "Panier", "images", List.of("image_url")),
                                                    "unit_amount", paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue()
                                            ),
                                            "quantity", 1
                                    )
                            ),
                            "mode", "payment",
                            "success_url", "http://localhost:5173/success",
                            "cancel_url", "http://localhost:5173/cancel"
                    )
            );

            // Mettre à jour le paiement avec l'ID de session Stripe et le sauvegarder
            payment.setStripeSessionId(session.getId());
            paymentRepository.save(payment);
            System.out.println("Payment Date: " + currentDate);
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
            return "Stripe error: " + e.getMessage();
        }
    }

    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
            payment.setStatus(status);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
        }
    }
}
