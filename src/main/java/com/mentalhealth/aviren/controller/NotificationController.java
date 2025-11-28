package com.mentalhealth.aviren.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<NotificationCount>> getNotificationCount(
            Authentication authentication) {
        
        String email = authentication.getName();
        NotificationCount response = notificationService.getNotificationCount(email);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan jumlah notifikasi", response));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            Authentication authentication,
            @PathVariable Long id) {
        
        String email = authentication.getName();
        notificationService.markAsRead(id, email);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi ditandai sudah dibaca", null));
    }
    
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            Authentication authentication) {
        
        String email = authentication.getName();
        notificationService.markAllAsRead(email);
        return ResponseEntity.ok(ApiResponse.success("Semua notifikasi ditandai sudah dibaca", null));
    }
}