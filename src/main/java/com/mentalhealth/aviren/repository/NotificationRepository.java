package com.mentalhealth.aviren.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mentalhealth.aviren.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(UUID userId);
    List<Notification> findByUserId(UUID userId);
    List<Notification> findByUserIdAndIsRead(UUID userId, Boolean isRead);
    Long countByUserId(UUID userId);
    Long countByUserIdAndIsRead(UUID userId, Boolean isRead);
}