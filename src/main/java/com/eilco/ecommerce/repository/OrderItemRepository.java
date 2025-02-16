package com.eilco.ecommerce.repository;

import com.eilco.ecommerce.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Trouver tous les OrderItems d'une commande spécifique
    List<OrderItem> findByOrderId(Long orderId);

}
