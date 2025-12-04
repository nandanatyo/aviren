package com.mentalhealth.aviren.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private UUID id;
    private String message;
    private String senderType;
    private LocalDateTime createdAt;
}