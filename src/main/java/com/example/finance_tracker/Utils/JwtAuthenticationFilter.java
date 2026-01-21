package com.example.finance_tracker.Utils;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Services.JwtService;
import com.example.finance_tracker.Services.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtService _jwtService;
    private final UserService _userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService){
        this._jwtService = jwtService;
        this._userService = userService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = _jwtService.validateAndGetClaims(token);

            Long userId = Long.valueOf(claims.getSubject());
            boolean emailVerirfied = claims.get("email_verified", Boolean.class);
            List<String> roles = claims.get("roles", List.class);
            
            if (!emailVerirfied){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            List<? extends GrantedAuthority> authorities =
                roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();

            User user = _userService.getUserById(userId);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (NumberFormatException ex){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
