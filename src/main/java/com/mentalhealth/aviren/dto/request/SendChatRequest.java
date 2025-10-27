package com.mentalhealth.aviren.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendChatRequest {
    
    @NotBlank(message = "Pesan tidak boleh kosong")
    private String message;
}