package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.MyEntity;

@Repository
//Repository берет trycatch+кучу методов (findById...) из JpaRepository
public interface MyRepository extends JpaRepository<MyEntity, Integer> {

}
