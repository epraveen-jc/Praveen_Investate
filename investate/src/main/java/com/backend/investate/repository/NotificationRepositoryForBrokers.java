// src/main/java/com/backend/investate/repository/NotificationRepositoryForBrokers.java
package com.backend.investate.repository;

import com.backend.investate.model.NotificationsForBroker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryForBrokers extends JpaRepository<NotificationsForBroker,Long> {
    List<NotificationsForBroker> findByBrokerNameOrderByCreatedDateDesc(String brokerName);
}
