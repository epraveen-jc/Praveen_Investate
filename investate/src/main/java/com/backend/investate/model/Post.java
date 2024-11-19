package com.backend.investate.model;
import java.time.LocalDateTime;

import com.backend.investate.enums.PropertyType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author E Praveen Kumar
 */
@Entity
@Data
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brokerName;
    private String phoneNumber;
    private String title;

    private String image; // Changed from String to byte[]
    private String streetOrColony;
    private String state;
    private String district;
    private String geolocation;
    private String description;
    private Double pricePerSqrFeet;
    private Double totalSqrFeet;
    private Double totalPrice;
    private Boolean isForSale;
    private Boolean isSold; 
    private String keyWords;
    private PropertyType propertyType; // New field for property type
     
    // New field for tracking sold status
    private LocalDateTime createdAt;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        
            this.image = image;
        
    }
    public Post() {
        
        this.createdAt = LocalDateTime.now();
        this.isForSale = true; // Default to true for new posts
        this.isSold = false;   // Default to false for new posts
    }
}
