package com.mentalhealth.aviren.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    
    @Size(min = 2, max = 100, message = "Nama harus antara 2-100 karakter")
    private String name;
    
    @Email(message = "Format email tidak valid")
    private String email;
}