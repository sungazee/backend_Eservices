package com.eilco.ecommerce.controller;
import com.eilco.ecommerce.dto.ProductRequest;
import com.eilco.ecommerce.dto.ProductResponse;
import com.eilco.ecommerce.model.entities.Product;
import com.eilco.ecommerce.service.ProductService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(@RequestBody ProductRequest productRequest) {
        return ProductResponse.builder().product(productService.save(productRequest)).build();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable("id") Long id){
        return productService.findById(id);
    }

    @GetMapping
    public List<Product> List() {
        return productService.findAll();
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@RequestBody ProductRequest productRequest, @PathVariable("id") Long id){
        return ProductResponse.builder().product(productService.update(productRequest, id)).build();
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteById(id);
    }


    @PutMapping("/{productId}/quantity")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        Optional<Product> updatedProduct = productService.updateQuantity(productId, productRequest);

        if (updatedProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Produit non trouvé !");
        }

        return ResponseEntity.ok(Map.of(
                "message", "Quantité du produit mise à jour avec succès",
                "productId", productId,
                "newQuantity", updatedProduct.get().getQuantity()
        ));
    }



    @PutMapping("/{productId}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long productId, @RequestBody Map<String, Boolean> request) {
        boolean isActive = request.get("active"); // Récupère l'état envoyé (true ou false)
        Optional<Product> updatedProduct = productService.toggleFavorite(productId, isActive);

        if (updatedProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Produit non trouvé !");
        }

        return ResponseEntity.ok(Map.of(
                "message", "Statut du favori mis à jour avec succès",
                "productId", productId,
                "active", updatedProduct.get().isActive()
        ));
    }
}