package com.eilco.ecommerce.model.entities;

public enum OrderStatus {
    PENDING,    // En attente de validation
    CONFIRMED,  // Commande confirmée
    SHIPPED,    // Expédiée
    DELIVERED,  // Livrée
    CANCELED    // Annulée
}