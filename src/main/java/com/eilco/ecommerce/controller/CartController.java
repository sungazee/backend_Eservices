package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.CartItemRequest;
import com.eilco.ecommerce.dto.CartResponse;
import com.eilco.ecommerce.model.entities.Cart;
import com.eilco.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addProductToCart(@RequestParam Long userId, @RequestBody CartItemRequest cartItemRequest) {
        return cartService.addProductToCart(userId, cartItemRequest);
    }

    // Endpoint pour créer un nouveau panier
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Cart createNewCart(@RequestParam Long userId) {
        return cartService.createNewCart(userId);
    }

    @PutMapping("/update/{cartItemId}")
    public CartResponse updateProductQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        return cartService.updateProductQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeProductFromCart(@PathVariable Long cartItemId) {
        cartService.removeProductFromCart(cartItemId);
    }

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }
}
