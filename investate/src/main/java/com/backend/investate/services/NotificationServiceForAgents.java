// src/main/java/com/backend/investate/service/NotificationServiceForAgents.java
package com.backend.investate.services;

import com.backend.investate.model.NotificationsForAgent;
import com.backend.investate.model.Post;
import com.backend.investate.repository.NotificationRepositoryForAgents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NotificationServiceForAgents {

    @Autowired
    private NotificationRepositoryForAgents notificationRepositoryForAgents;

    public NotificationsForAgent createNotification(Long postId, String clientName, String agentName, String clientPhoneNumber) {
        NotificationsForAgent notification = new NotificationsForAgent();
        notification.setPostId(postId);
        notification.setClientName(clientName);
        notification.setAgentName(agentName);
        notification.setClientPhoneNumber(clientPhoneNumber);
        notification.setMessage("Client " + clientName + " is interested in your post " + postId);
        return notificationRepositoryForAgents.save(notification);
    }

    public List<NotificationsForAgent> getNotificationsForAgent(String agentName) {
        System.out.println(notificationRepositoryForAgents.findByAgentName(agentName));
        return notificationRepositoryForAgents.findByAgentName(agentName);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepositoryForAgents.deleteById(notificationId);
    }

    public void acceptDeal(Long notificationId) {
        NotificationsForAgent notification = notificationRepositoryForAgents.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        // Additional logic to handle the deal acceptance (e.g., notify client) can be added here.
        notificationRepositoryForAgents.save(notification);
    }
}
