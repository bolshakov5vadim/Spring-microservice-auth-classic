package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t1")
public class MyEntity {
  @Id
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "likes")
  private int likes;
  
  public MyEntity() {
    super();
  }
  public MyEntity(int id, String name, String description, int likes) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.likes = likes;
  }
  
  public int getId() {
   return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public int getLikes() {
    return likes;
  }
  public void setLikes(int likes) {
    this.likes = likes;
  }
  @Override
  public String toString() {
    return "Entity [id=" + id + ", name=" + name + ", description=" + description + ", likes=" + likes + "]";
  }
}
