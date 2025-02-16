package com.eilco.ecommerce.repository;

import com.eilco.ecommerce.model.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);  // Trouver un panier par utilisateur
}
