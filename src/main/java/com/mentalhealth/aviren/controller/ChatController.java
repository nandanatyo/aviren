package com.mentalhealth.aviren.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mentalhealth.aviren.dto.request.SendChatRequest;
import com.mentalhealth.aviren.dto.response.ApiResponse;
import com.mentalhealth.aviren.dto.response.ChatResponse;
import com.mentalhealth.aviren.service.ChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatResponse>>> getChatHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        String email = authentication.getName();
        List<ChatResponse> response = chatService.getChatHistory(email, page, size);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan riwayat chat", response));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> sendMessage(
            Authentication authentication,
            @Valid @RequestBody SendChatRequest request) {
        
        String email = authentication.getName();
        ChatResponse response = chatService.sendMessage(email, request);
        return ResponseEntity.ok(ApiResponse.success("Pesan berhasil dikirim", response));
    }
}