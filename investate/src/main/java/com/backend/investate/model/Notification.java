// src/main/java/com/backend/investate/model/Notification.java
package com.backend.investate.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
/**
 * @author E Praveen Kumar
 */
@Entity
@Data
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private String clientName;
    private String agentName;
    
    private LocalDateTime createdAt;

    private String message;  // Example: "Client <clientName> is interested in your post <postId>"

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }
}
