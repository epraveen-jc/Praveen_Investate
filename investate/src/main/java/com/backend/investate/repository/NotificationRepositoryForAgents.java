// src/main/java/com/backend/investate/repository/NotificationRepositoryForAgents.java
package com.backend.investate.repository;

import com.backend.investate.model.NotificationsForAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryForAgents extends JpaRepository<NotificationsForAgent,Long> {
    List<NotificationsForAgent> findByAgentName(String agentName);
}
