package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  private int id;
  @Column(name = "username")
  private String username;
  @Column(name = "hash")
  private String hash;
  @Column(name = "role")
  private int role;
  
  public UserEntity() {
    super();
  }
  public UserEntity(int id, String username, String hash, int role) {
    super();
    this.id = id;
    this.username = username;
    this.hash = hash;
    this.role = role;
  }
  
  public int getId() {
   return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getHash() {
    return hash;
  }
  public void setHash(String hash) {
    this.hash = hash;
  }
  public int getRole() {
    return role;
  }
  public void setRole(int role) {
    this.role = role;
  }
  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", hash=" + hash + ", role=" + role + "]";
  }
}
