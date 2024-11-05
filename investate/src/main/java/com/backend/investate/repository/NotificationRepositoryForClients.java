// src/main/java/com/backend/investate/repository/NotificationRepositoryForClients.java
package com.backend.investate.repository;

import com.backend.investate.model.NotificationsForClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryForClients extends JpaRepository<NotificationsForClient, Long> {
    List<NotificationsForClient> findByClientName(String clientName);
}
