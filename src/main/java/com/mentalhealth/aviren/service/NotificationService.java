package com.mentalhealth.aviren.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalhealth.aviren.dto.response.NotificationResponse;
import com.mentalhealth.aviren.entity.Notification;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.exception.BadRequestException;
import com.mentalhealth.aviren.exception.ResourceNotFoundException;
import com.mentalhealth.aviren.repository.NotificationRepository;
import com.mentalhealth.aviren.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    /**
     * Get all notifications for a user
     */
    public List<NotificationResponse> getNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());
        
        return notifications.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get only unread notifications for a user
     */
    public List<NotificationResponse> getUnreadNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        List<Notification> notifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());
        
        return notifications.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get notification count (total and unread)
     */
    public NotificationCount getNotificationCount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        Long totalCount = notificationRepository.countByUserId(user.getId());
        Long unreadCount = notificationRepository.countByUserIdAndIsRead(user.getId(), false);
        
        return new NotificationCount(totalCount, unreadCount);
    }
    
    /**
     * Get only unread count (useful for badge display)
     */
    public Long getUnreadCount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        return notificationRepository.countByUserIdAndIsRead(user.getId(), false);
    }
    
    /**
     * Create a new notification
     */
    @Transactional
    public void createNotification(UUID userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);
    }
    
    /**
     * Mark a specific notification as read
     */
    @Transactional
    public void markAsRead(UUID notificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notifikasi tidak ditemukan"));
        
        if (!notification.getUserId().equals(user.getId())) {
            throw new BadRequestException("Tidak dapat mengakses notifikasi ini");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
    
    /**
     * Mark all notifications as read
     */
    @Transactional
    public void markAllAsRead(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());
        
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    
    /**
     * Delete a specific notification
     */
    @Transactional
    public void deleteNotification(UUID notificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notifikasi tidak ditemukan"));
        
        if (!notification.getUserId().equals(user.getId())) {
            throw new BadRequestException("Tidak dapat mengakses notifikasi ini");
        }
        
        notificationRepository.delete(notification);
    }
    
    /**
     * Delete all read notifications
     */
    @Transactional
    public void clearReadNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        List<Notification> readNotifications = notificationRepository
                .findByUserIdAndIsRead(user.getId(), true);
        
        notificationRepository.deleteAll(readNotifications);
    }
    
    /**
     * Map Notification entity to NotificationResponse DTO
     */
    private NotificationResponse mapToNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
    
    /**
     * Inner class for notification count response
     */
    public static class NotificationCount {
        private Long total;
        private Long unread;
        
        public NotificationCount(Long total, Long unread) {
            this.total = total;
            this.unread = unread;
        }
        
        public Long getTotal() {
            return total;
        }
        
        public Long getUnread() {
            return unread;
        }
        
        public void setTotal(Long total) {
            this.total = total;
        }
        
        public void setUnread(Long unread) {
            this.unread = unread;
        }
    }
}