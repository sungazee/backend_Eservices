package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.CartItemRequest;
import com.eilco.ecommerce.dto.CartItemResponse;
import com.eilco.ecommerce.dto.CartResponse;
import com.eilco.ecommerce.model.entities.Cart;
import com.eilco.ecommerce.model.entities.CartItem;
import com.eilco.ecommerce.model.entities.Product;
import com.eilco.ecommerce.model.entities.User;
import com.eilco.ecommerce.repository.CartRepository;
import com.eilco.ecommerce.repository.CartItemRepository;
import com.eilco.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;



    public CartResponse addProductToCart(Long userId, CartItemRequest cartItemRequest) {
        // Récupérer le panier de l'utilisateur
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Récupérer le produit à ajouter
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérifier si l'article existe déjà dans le panier
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + cartItemRequest.getQuantity());
            item.setPriceAtTime(product.getPrice());
            cartItemRepository.save(item);
        } else {
            // Ajouter un nouvel article au panier
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemRequest.getQuantity())
                    .priceAtTime(product.getPrice())
                    .build();
            cartItemRepository.save(cartItem);
            cart.getCartItems().add(cartItem);
        }

        // Sauvegarder les modifications du panier
        cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    public CartResponse updateProductQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Article du panier non trouvé"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return convertToCartResponse(cartItem.getCart());
    }

    public void removeProductFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Article du panier non trouvé"));
        cartItemRepository.delete(cartItem);
    }

    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));
        return convertToCartResponse(cart);
    }

    public Cart createNewCart(Long userId) {
        // Créer un panier vide pour un nouvel utilisateur
        Cart newCart = Cart.builder()
                .user(new User(userId))  // Assurez-vous d'avoir une classe User avec l'ID utilisateur
                .build();
        return cartRepository.save(newCart);
    }
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    private CartResponse convertToCartResponse(Cart cart) {
        List<CartItemResponse> cartItemResponses = cart.getCartItems().stream()
                .map(cartItem -> CartItemResponse.builder()
                        .id(cartItem.getId())
                        .productId(cartItem.getProduct().getId())
                        .productName(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .priceAtTime(cartItem.getPriceAtTime())
                        .subTotal(cartItem.getSubTotal())
                        .build())
                .toList();

        BigDecimal totalAmount = cartItemResponses.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .cartItems(cartItemResponses)
                .totalAmount(totalAmount)
                .build();
    }
}
