package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // Добавляем метод для поиска по username. Реализация автоматическая, по имени
    Optional<UserEntity> findByUsername(String username);
    
    // Можно добавить другие методы при необходимости
    boolean existsByUsername(String username);
}