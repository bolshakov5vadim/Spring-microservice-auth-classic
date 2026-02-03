package com.example.service;

import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                int role = jwtService.getRoleFromToken(token);
                
                // Проверяем существование пользователя в БД
                Optional<UserEntity> userOptional = userRepository.findByUsername(username);
                
                if (userOptional.isPresent()) {
                    UserEntity user = userOptional.get();
                    
                    // Дополнительно можно проверить совпадение роли из токена и из БД. Защита от подделки токена
                    // Здесь не нужно authentification. Он только при генерации токена
                    if (user.getRole() == role) {
                        String roleName = getRoleName(role);
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                Collections.singletonList(new SimpleGrantedAuthority(roleName))
                            );
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getRoleName(int role) {
        // Преобразуем числовую роль в строковое представление
        switch (role) {
            case 1: return "ROLE_ADMIN";
            case 2: return "ROLE_MODERATOR";
            case 3: return "ROLE_USER";
            default: return "ROLE_GUEST";
        }
    }
}