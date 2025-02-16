package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.OrderItemRequest;
import com.eilco.ecommerce.dto.OrderItemResponse;
import com.eilco.ecommerce.model.entities.Order;
import com.eilco.ecommerce.model.entities.OrderItem;
import com.eilco.ecommerce.model.entities.Product;
import com.eilco.ecommerce.repository.OrderItemRepository;
import com.eilco.ecommerce.repository.OrderRepository;
import com.eilco.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 1️⃣ Créer un OrderItem
    public OrderItemResponse createOrderItem(Long orderId, OrderItemRequest orderItemRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        Product product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(orderItemRequest.getQuantity())
                .priceAtPurchase(product.getPrice()) // Le prix au moment de l'achat
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return convertToResponse(savedOrderItem);
    }

    // 2️⃣ Mettre à jour un OrderItem
    public OrderItemResponse updateOrderItem(Long id, OrderItemRequest orderItemRequest) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article de commande non trouvé"));

        Product product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequest.getQuantity());
        orderItem.setPriceAtPurchase(product.getPrice());

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return convertToResponse(updatedOrderItem);
    }

    // 3️⃣ Supprimer un OrderItem
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    // 4️⃣ Récupérer un OrderItem par ID
    public Optional<OrderItemResponse> getOrderItemById(Long id) {
        return orderItemRepository.findById(id).map(this::convertToResponse);
    }

    // 5️⃣ Récupérer tous les OrderItems d'une commande
    public List<OrderItemResponse> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 🔄 Conversion en DTO (OrderItemResponse)
    private OrderItemResponse convertToResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .priceAtPurchase(orderItem.getPriceAtPurchase())
                .subTotal(orderItem.getSubTotal())  // Calcul du sous-total
                .build();
    }
}
