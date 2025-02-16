package com.eilco.ecommerce.service;

import com.eilco.ecommerce.dto.UserRequest;
import com.eilco.ecommerce.dto.UserResponse;
import com.eilco.ecommerce.model.entities.User;
import com.eilco.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    // Création d'un utilisateur sans hachage du mot de passe
    public UserResponse save(UserRequest userRequest) {
        User user = convertToUser(userRequest, null);
        // Pas de hachage : le mot de passe est stocké tel quel
        user = repository.save(user);
        return convertToResponse(user);
    }

    // Mise à jour d'un utilisateur existant sans hachage du mot de passe
    public UserResponse update(UserRequest userRequest, Long id) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setAddress(userRequest.getAddress());
        existingUser.setPhoneNumber(userRequest.getPhoneNumber());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            // Le mot de passe est mis à jour sans être encodé
            existingUser.setPassword(userRequest.getPassword());
        }

        repository.save(existingUser);
        return convertToResponse(existingUser);
    }

    // Suppression d'un utilisateur
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        repository.deleteById(id);
    }

    // Recherche par ID
    public Optional<UserResponse> findById(Long id) {
        return repository.findById(id).map(this::convertToResponse);
    }

    // Obtenir tous les utilisateurs
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Convertir un UserRequest en User
    private User convertToUser(UserRequest request, Long id) {
        return User.builder()
                .id(id)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword()) // Le mot de passe est stocké tel quel
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    // Convertir un User en UserResponse
    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
