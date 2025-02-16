package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.OrderRequest;
import com.eilco.ecommerce.dto.OrderResponse;
import com.eilco.ecommerce.model.entities.*;
import com.eilco.ecommerce.repository.OrderRepository;
import com.eilco.ecommerce.repository.PaymentRepository;
import com.eilco.ecommerce.repository.ProductRepository;
import com.eilco.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 1️⃣ Créer une commande
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO) // Initialisé à zéro avant calcul
                .orderItems(List.of()) // Les articles seront ajoutés ensuite
                .build();

        // Enregistrer la commande
        Order savedOrder = orderRepository.save(order);

        // Calculer le montant total et mettre à jour
        BigDecimal totalAmount = calculateTotalAmount(savedOrder);
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        return convertToResponse(savedOrder);
    }

    // 2️⃣ Modifier une commande
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        // Si on change les articles, recalculer le total
        BigDecimal newTotal = calculateTotalAmount(order);
        order.setTotalAmount(newTotal);

        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    // 3️⃣ Supprimer une commande
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // 4️⃣ Récupérer une commande par ID
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToResponse);
    }

    // 5️⃣ Récupérer toutes les commandes
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 6️⃣ Récupérer les commandes d’un utilisateur
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 7️⃣ Modifier le statut d’une commande
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        order.setStatus(newStatus);
        return convertToResponse(orderRepository.save(order));
    }



    // 8️⃣ Calcul du montant total d’une commande
    private BigDecimal calculateTotalAmount(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 🔄 Conversion en DTO
    private OrderResponse convertToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderItems(List.of()) // À remplacer par une conversion des OrderItem
                .build();
    }
}
