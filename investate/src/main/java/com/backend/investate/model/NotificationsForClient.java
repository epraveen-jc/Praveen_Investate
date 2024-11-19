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
public class NotificationsForClient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String clientName;
    private String brokerName;
    private String brokerPhoneNumber;
    private LocalDateTime createdAt; // New field for tracking deal acceptance
    @Column(length =2000)
    private String message;  // Example: "Client <clientName> is interested in your post <postId>"
    public NotificationsForClient() {
        this.createdAt = LocalDateTime.now();
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
