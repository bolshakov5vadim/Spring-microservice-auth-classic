package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.entity.MyEntity;//название файла. подключили сущность 
import com.example.repository.MyRepository;//название файла. подключили репозиторий

import java.util.HashMap;
import java.util.Map;
import java.util.List;
//import org.json.simple.JSONObject; //its EXTRA library

@RestController
public class SecretController {

    @Autowired
    private MyRepository repository;

    @GetMapping("/secret")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSecretData(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        
        // String signedUrl = s3Service.generatePresignedUrl(imageId, Duration.ofMinutes(5));
        // Если у вас хранилище медиафайлов, используют библиотеку s3Service
        
        List<MyEntity> list = repository.findAll();
        //JSONObject json = new JSONObject();
        
        Map<String, List> response = new HashMap<>();
        response.put("message", List.of("This is secret data"));
        response.put("user", List.of(email));
        response.put("data", list);
        
        return ResponseEntity.ok(response);
    }
}
