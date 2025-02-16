package com.eilco.ecommerce.controller;

import com.eilco.ecommerce.dto.UserRequest;
import com.eilco.ecommerce.dto.UserResponse;
import com.eilco.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")  // Convention REST : Pluriel pour les ressources
public class UserController {

    private final UserService userService;

    // Création d'un utilisateur
    @PostMapping
    public ResponseEntity<UserResponse> createUser( @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.save(userRequest);
        return ResponseEntity.status(201).body(createdUser); // 201 Created
    }

    // Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé")); // Meilleure gestion d'erreur possible
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users); // 200 OK
    }

    // Mise à jour d'un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable Long id) {
        UserResponse updatedUser = userService.update(userRequest, id);
        return ResponseEntity.ok(updatedUser); // 200 OK
    }

    // Suppression d'un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
