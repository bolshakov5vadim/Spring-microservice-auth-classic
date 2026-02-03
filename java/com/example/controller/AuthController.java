package com.example.controller;

import com.example.service.AuthService; // здесь используем AuthService.authenticate
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    // Логин
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Optional<String> token = authService.authenticate(
            authRequest.getUsername(), 
            authRequest.getPassword()
        );
        
        return createAuthResponse(token);
    }
    
    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Optional<String> token = authService.register(
            registerRequest.getUsername(),
            registerRequest.getPassword(),
            registerRequest.getRole() // По умолчанию можно установить роль USER
        );
        
        return createAuthResponse(token);
    }
    
    // Проверка токена (валидация)
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            Optional<com.example.entity.UserEntity> userOptional = 
                authService.getUserFromToken(token);
            
            if (userOptional.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", userOptional.get().getUsername());
                response.put("role", userOptional.get().getRole());
                return ResponseEntity.ok(response);
            }
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("valid", "false");
        error.put("error", "Invalid or expired token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    
    // Вспомогательный метод для создания ответа
    private ResponseEntity<?> createAuthResponse(Optional<String> token) {
        if (token.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("token", token.get());
            response.put("message", "Success");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    // Классы
    public static class AuthRequest {
        private String username;
        private String password;
        
        // Геттеры и сеттеры
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class RegisterRequest extends AuthRequest {
        private int role = 3; // По умолчанию роль USER
        
        public int getRole() { return role; }
        public void setRole(int role) { this.role = role; }
    }
}