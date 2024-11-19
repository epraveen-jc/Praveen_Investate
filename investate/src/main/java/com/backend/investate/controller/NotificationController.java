package com.backend.investate.controller;
// src/main/java/com/backend/investate/controller/NotificationController.java


import com.backend.investate.model.NotificationsForBroker;
import com.backend.investate.model.NotificationsForClient;
import com.backend.investate.services.NotificationServiceForBrokers;
import com.backend.investate.services.NotificationServiceForClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin as necessary
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationServiceForBrokers notificationServiceForBrokers;

    @Autowired
    private NotificationServiceForClients notificationServiceForClients;

    @GetMapping("/broker/{brokerName}")
    public ResponseEntity<List<NotificationsForBroker>> getBrokerNotifications(@PathVariable String brokerName) {
        return ResponseEntity.ok(notificationServiceForBrokers.getNotificationsForBroker(brokerName));
    }

    @GetMapping("/client/{clientName}")
    public ResponseEntity<List<NotificationsForClient>> getClientNotifications(@PathVariable String clientName) {
        return ResponseEntity.ok(notificationServiceForClients.getNotificationsForClient(clientName));
    }

    // deal button 
    @PostMapping("/create-broker-notification")
    public ResponseEntity<NotificationsForBroker> createBrokerNotification(
            @RequestParam Long postId,
            @RequestParam String clientName,
            @RequestParam String brokerName,
            @RequestParam String clientPhoneNumber) {
                System.out.println("post id "+postId+postId.TYPE);
        NotificationsForBroker notification = notificationServiceForBrokers.createNotification(postId, clientName, brokerName, clientPhoneNumber);
        return ResponseEntity.ok(notification);
    }

    //deal accept button
    @PutMapping("/accept/{notificationId}")
    public ResponseEntity<NotificationsForClient> createClientNotification(@RequestParam Long postId,
    @RequestParam String  brokerName,
    @RequestParam String clientName,
    @RequestParam String brokerPhoneNumber) {
        NotificationsForClient notification = notificationServiceForClients.notifyClient(postId,brokerName , clientName,brokerPhoneNumber);
        return ResponseEntity.ok(notification);
    }
    
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationServiceForBrokers.deleteNotification(notificationId);
            return ResponseEntity.ok("Notification deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete notification: " + e.getMessage());
        }
    }
}
