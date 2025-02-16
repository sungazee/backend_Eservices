package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.OrderRequest;
import com.eilco.ecommerce.dto.OrderResponse;
import com.eilco.ecommerce.model.entities.OrderStatus;
import com.eilco.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    // 1️⃣ Créer une commande
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    // 2️⃣ Récupérer une commande par ID
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    // 3️⃣ Récupérer toutes les commandes
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 4️⃣ Récupérer toutes les commandes d’un utilisateur
    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    // 5️⃣ Mettre à jour une commande
    @PutMapping("/{id}")
    public OrderResponse updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest);
    }

    // 6️⃣ Modifier le statut d’une commande
    @PatchMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }

    // 7️⃣ Supprimer une commande
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
