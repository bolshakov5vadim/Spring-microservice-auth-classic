package com.example.service;

import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    
    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder, 
                      JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    // Аутентификация пользователя
    public Optional<String> authenticate(String username, String password) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            
            // Проверяем хэш пароля
            if (passwordEncoder.matches(password, user.getHash())) {
                // Генерируем JWT токен
                String token = jwtService.generateToken(user.getUsername(), user.getRole());
                return Optional.of(token);
            }
        }
        
        return Optional.empty();
    }
    
    // Регистрация нового пользователя
    @Transactional
    public Optional<String> register(String username, String password, int role) {
        // Проверяем, не существует ли уже пользователь
        if (userRepository.existsByUsername(username)) {
            return Optional.empty();
        }
        
        // Создаем нового пользователя
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setHash(passwordEncoder.encode(password)); // Хешируем пароль
        user.setRole(role);
        
        // Сохраняем в БД (id сгенерируется автоматически, если есть @GeneratedValue)
        UserEntity savedUser = userRepository.save(user);
        
        // Генерируем токен для нового пользователя
        String token = jwtService.generateToken(savedUser.getUsername(), savedUser.getRole());
        return Optional.of(token);
    }
    
    // Получение информации о пользователе по токену
    public Optional<UserEntity> getUserFromToken(String token) {
        if (jwtService.validateToken(token)) {
            String username = jwtService.getUsernameFromToken(token);
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }
}