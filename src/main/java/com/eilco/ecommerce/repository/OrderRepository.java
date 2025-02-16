package com.eilco.ecommerce.repository;


import com.eilco.ecommerce.model.entities.Order;
import com.eilco.ecommerce.model.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId); // Récupérer les commandes d’un utilisateur

}