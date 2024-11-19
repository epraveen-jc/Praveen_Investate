package com.backend.investate.services;
// src/main/java/com/backend/investate/service/NotificationServiceForClients.java


import com.backend.investate.model.NotificationsForClient;
import com.backend.investate.repository.NotificationRepositoryForClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceForClients {

    @Autowired
    private NotificationRepositoryForClients notificationRepositoryForClients;

    public NotificationsForClient notifyClient(Long postId, String brokerName, String clientName, String brokerPhoneNumber) {
        NotificationsForClient notification = new NotificationsForClient();
        notification.setPostId(postId);
        notification.setClientName(clientName);
        notification.setBrokerName(brokerName);
        notification.setBrokerPhoneNumber(brokerPhoneNumber);
        notification.setMessage("Broker " + brokerName + " accepted your deal request for post " + postId + " he will contact you soon.");
        return notificationRepositoryForClients.save(notification);
    }

    public List<NotificationsForClient> getNotificationsForClient(String clientName) {
        return notificationRepositoryForClients.findByClientName(clientName);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepositoryForClients.deleteById(notificationId);
    }
}
