package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.OrderItemRequest;
import com.eilco.ecommerce.dto.OrderItemResponse;
import com.eilco.ecommerce.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders/{orderId}/items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    // 1️⃣ Créer un OrderItem
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemResponse createOrderItem(
            @PathVariable Long orderId,
            @RequestBody OrderItemRequest orderItemRequest) {
        return orderItemService.createOrderItem(orderId, orderItemRequest);
    }

    // 2️⃣ Mettre à jour un OrderItem
    @PutMapping("/{itemId}")
    public OrderItemResponse updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestBody OrderItemRequest orderItemRequest) {
        return orderItemService.updateOrderItem(itemId, orderItemRequest);
    }

    // 3️⃣ Supprimer un OrderItem
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItem(@PathVariable Long itemId) {
        orderItemService.deleteOrderItem(itemId);
    }

    // 4️⃣ Récupérer un OrderItem par ID
    @GetMapping("/{itemId}")
    public OrderItemResponse getOrderItem(@PathVariable Long itemId) {
        return orderItemService.getOrderItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Article de commande non trouvé"));
    }

    // 5️⃣ Récupérer tous les OrderItems d'une commande
    @GetMapping
    public List<OrderItemResponse> getAllOrderItems(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }
}
