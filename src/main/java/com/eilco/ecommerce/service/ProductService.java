package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.ProductRequest;
import com.eilco.ecommerce.model.entities.Product;
import com.eilco.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    public Product save(ProductRequest productRequest) {
        return repository.save(convertProductRequestToProduct(productRequest, null));
    }

    public Product update(ProductRequest productRequest, Long id) {
        return repository.save(convertProductRequestToProduct(productRequest, id));
    }

    public Optional<Product> updateQuantity(Long productId, ProductRequest productRequest) {
        Optional<Product> productOptional = findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setQuantity(productRequest.getQuantity()); // Met à jour la quantité

            return Optional.of(repository.save(product)); // Sauvegarde du produit mis à jour
        }

        return Optional.empty();
    }



    public void deleteById(Long id) { repository.deleteById(id);}

    public Optional<Product> findById(Long id) { return repository.findById(id);}

    public List<Product> findAll() {return (List<Product>) repository.findAll();}

    private Product convertProductRequestToProduct(ProductRequest productRequest, Long id) {
        return Product.builder()
                .id(id)
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .description(productRequest.getDescription())
                .active(productRequest.isActive())
                .imageUrl(productRequest.getImageUrl())
                .build();
    }


    public Optional<Product> toggleFavorite(Long productId, boolean isActive) {
        return repository.findById(productId).map(product -> {
            product.setActive(isActive); // Met à jour l'état actif (favori)
            return repository.save(product);
        });
    }
}
