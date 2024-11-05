package com.backend.investate.controller;
// src/main/java/com/backend/investate/controller/NotificationController.java


import com.backend.investate.model.NotificationsForAgent;
import com.backend.investate.model.NotificationsForClient;
import com.backend.investate.services.NotificationServiceForAgents;
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
    private NotificationServiceForAgents notificationServiceForAgents;

    @Autowired
    private NotificationServiceForClients notificationServiceForClients;

    @GetMapping("/agent/{agentName}")
    public ResponseEntity<List<NotificationsForAgent>> getAgentNotifications(@PathVariable String agentName) {
        return ResponseEntity.ok(notificationServiceForAgents.getNotificationsForAgent(agentName));
    }

    @GetMapping("/client/{clientName}")
    public ResponseEntity<List<NotificationsForClient>> getClientNotifications(@PathVariable String clientName) {
        return ResponseEntity.ok(notificationServiceForClients.getNotificationsForClient(clientName));
    }

    // deal button 
    @PostMapping("/create-agent-notification")
    public ResponseEntity<NotificationsForAgent> createAgentNotification(
            @RequestParam Long postId,
            @RequestParam String clientName,
            @RequestParam String agentName,
            @RequestParam String clientPhoneNumber) {
                System.out.println("post id "+postId+postId.TYPE);
        NotificationsForAgent notification = notificationServiceForAgents.createNotification(postId, clientName, agentName, clientPhoneNumber);
        return ResponseEntity.ok(notification);
    }

    //deal accept button
    @PutMapping("/accept/{notificationId}")
    public ResponseEntity<NotificationsForClient> createClientNotification(@RequestParam Long postId,
    @RequestParam String  agentName,
    @RequestParam String clientName,
    @RequestParam String agentPhoneNumber) {
        NotificationsForClient notification = notificationServiceForClients.notifyClient(postId,agentName , clientName,agentPhoneNumber);
        return ResponseEntity.ok(notification);
    }
    
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationServiceForAgents.deleteNotification(notificationId);
            return ResponseEntity.ok("Notification deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete notification: " + e.getMessage());
        }
    }
}
