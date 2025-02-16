package com.eilco.ecommerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class NoSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF (souvent désactivée pour les API REST)
                .csrf(csrf -> csrf.disable())
                // Autorise toutes les requêtes sans authentification
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                // Désactive httpBasic et formLogin pour ne pas générer de challenge d'authentification
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable()).cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    return configuration;
}));
        return http.build();
    }
}
