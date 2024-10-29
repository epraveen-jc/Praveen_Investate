// src/main/java/com/backend/investate/services/NotificationService.java
package com.backend.investate.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.investate.model.Notification;
import com.backend.investate.repository.NotificationRepository;
/**
 * @author E Praveen Kumar
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Long postId, String clientName, String agentName) {
        Notification notification = new Notification();
        notification.setPostId(postId);
        notification.setClientName(clientName);
        notification.setAgentName(agentName);
        notification.setMessage("Client " + clientName + " is interested in your post " + postId);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForAgent(String agentName) {
        return notificationRepository.findByAgentNameOrderByCreatedAtDesc(agentName);
    }
}
