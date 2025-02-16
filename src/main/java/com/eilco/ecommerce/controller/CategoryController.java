package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.CategoryRequest;
import com.eilco.ecommerce.dto.CategoryResponse;
import com.eilco.ecommerce.model.entities.Category;
import com.eilco.ecommerce.model.entities.Product;
import com.eilco.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestBody CategoryRequest categoryRequest) {
        return CategoryResponse.builder().category(categoryService.save(categoryRequest)).build();
    }

    //correction ("/{id}")
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        // Récupérer la catégorie en fonction de son ID
        Optional<Category> categoryOpt = categoryService.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();  // Retourner 404 si la catégorie n'existe pas
        }

        // Récupérer les produits associés à la catégorie
        List<Product> products = categoryOpt.get().getProducts();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();  // Retourner 204 si aucun produit n'est associé
        }

        return ResponseEntity.ok(products);  // Retourner les produits de la catégorie
    }


    @GetMapping
    public List<Category> List() {
        return categoryService.findAll();
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable("id") Long id){
        return CategoryResponse.builder().category(categoryService.update(categoryRequest, id)).build();
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteById(id);
    }
}