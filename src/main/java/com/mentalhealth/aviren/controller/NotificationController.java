package com.mentalhealth.aviren.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mentalhealth.aviren.dto.response.ApiResponse;
import com.mentalhealth.aviren.dto.response.NotificationResponse;
import com.mentalhealth.aviren.service.NotificationService;
import com.mentalhealth.aviren.service.NotificationService.NotificationCount;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    /**
     * Get all notifications or only unread notifications
     * @param authentication - User authentication
     * @param unreadOnly - Filter untuk hanya notifikasi belum dibaca
     * @return List of notifications
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "false") Boolean unreadOnly) {
        
        String email = authentication.getName();
        
        List<NotificationResponse> response;
        String message;
        
        if (unreadOnly) {
            response = notificationService.getUnreadNotifications(email);
            message = "Berhasil mendapatkan notifikasi yang belum dibaca";
        } else {
            response = notificationService.getNotifications(email);
            message = "Berhasil mendapatkan semua notifikasi";
        }
        
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
    
    /**
     * Get total and unread notification count
     * @param authentication - User authentication
     * @return NotificationCount object
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<NotificationCount>> getNotificationCount(
            Authentication authentication) {
        
        String email = authentication.getName();
        NotificationCount response = notificationService.getNotificationCount(email);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan jumlah notifikasi", response));
    }
    
    /**
     * Get only unread notification count (for badge)
     * @param authentication - User authentication
     * @return Unread count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            Authentication authentication) {
        
        String email = authentication.getName();
        Long unreadCount = notificationService.getUnreadCount(email);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan jumlah notifikasi belum dibaca", unreadCount));
    }
    
    /**
     * Mark a specific notification as read
     * @param authentication - User authentication
     * @param id - Notification ID
     * @return Success response
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            Authentication authentication,
            @PathVariable UUID id) {
        
        String email = authentication.getName();
        notificationService.markAsRead(id, email);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi ditandai sudah dibaca", null));
    }
    
    /**
     * Mark all notifications as read
     * @param authentication - User authentication
     * @return Success response
     */
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            Authentication authentication) {
        
        String email = authentication.getName();
        notificationService.markAllAsRead(email);
        return ResponseEntity.ok(ApiResponse.success("Semua notifikasi ditandai sudah dibaca", null));
    }
    
    /**
     * Delete a specific notification
     * @param authentication - User authentication
     * @param id - Notification ID
     * @return Success response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            Authentication authentication,
            @PathVariable UUID id) {
        
        String email = authentication.getName();
        notificationService.deleteNotification(id, email);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi berhasil dihapus", null));
    }
    
    /**
     * Delete all read notifications
     * @param authentication - User authentication
     * @return Success response
     */
    @DeleteMapping("/clear-read")
    public ResponseEntity<ApiResponse<Void>> clearReadNotifications(
            Authentication authentication) {
        
        String email = authentication.getName();
        notificationService.clearReadNotifications(email);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi yang sudah dibaca berhasil dihapus", null));
    }
}