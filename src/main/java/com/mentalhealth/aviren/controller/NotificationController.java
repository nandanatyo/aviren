package com.mentalhealth.aviren.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mentalhealth.aviren.dto.response.ApiResponse;
import com.mentalhealth.aviren.dto.response.NotificationResponse;
import com.mentalhealth.aviren.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            Authentication authentication) {
        
        String email = authentication.getName();
        List<NotificationResponse> response = notificationService.getNotifications(email);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan notifikasi", response));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            Authentication authentication,
            @PathVariable Long id) {
        
        String email = authentication.getName();
        notificationService.markAsRead(id, email);
        return ResponseEntity.ok(ApiResponse.success("Notifikasi ditandai sudah dibaca", null));
    }
}