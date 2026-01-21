package com.example.finance_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.finance_tracker.Utils.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthEndpointConfig {

    private final JwtAuthenticationFilter _jwtAuthenticationFilter;

    public AuthEndpointConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this._jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/signup",
                    "/auth/login",
                    "/auth/verify-email",
                    "/auth/request-password-reset",
                    "/auth/confirm-password-reset"
                ).permitAll() // public endpoints
                .anyRequest().authenticated() // everything else requires auth
            ).addFilterBefore(_jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
