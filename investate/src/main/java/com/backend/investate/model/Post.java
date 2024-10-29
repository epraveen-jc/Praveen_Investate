package com.backend.investate.model;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String agentName;
    private int phoneNumber;
    private String title;
    private String image;
    private String placeName;
    private String geolocation;
    private String description;
    private Double pricePerSqrFeet;
    private Double totalSqrFeet;
    private Boolean isForSale;
    private Boolean isSold;  // New field for tracking sold status
    private LocalDateTime createdAt;
    
    public Post() {
        
        this.createdAt = LocalDateTime.now();
        this.isForSale = true; // Default to true for new posts
        this.isSold = false;   // Default to false for new posts
    }
}
