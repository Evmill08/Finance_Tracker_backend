package com.example.finance_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class AuthEndpointConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/signup",
                    "/auth/login",
                    "/auth/verify-email",
                    "/auth/request-password-reset",
                    "/auth/confirm-password-reset"
                ).permitAll() // public endpoints
                .anyRequest().authenticated() // everything else requires auth
            );
        return http.build();
    }
}
