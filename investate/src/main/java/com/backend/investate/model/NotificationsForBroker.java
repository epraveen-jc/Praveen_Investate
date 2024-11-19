// src/main/java/com/backend/investate/model/Notification.java
package com.backend.investate.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 * @author E Praveen Kumar
 */
@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
public class NotificationsForBroker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String brokerName;
    private String clientName;
    private String clientPhoneNumber;
    private LocalDateTime createdDate;
    @Column(length =2000)
    private String message;  // Example: "Client <clientName> is interested in your post <postId>"
    
    public NotificationsForBroker() {
        this.createdDate = LocalDateTime.now();
    }

    public void setId(Long postId) {
        this.postId = postId;
    }
    
}
