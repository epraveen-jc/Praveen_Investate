// src/main/java/com/backend/investate/repository/NotificationRepository.java
package com.backend.investate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.investate.model.Notification;
/**
 * @author E Praveen Kumar
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByAgentNameOrderByCreatedAtDesc(String agentName); // Get notifications for an agent
}
