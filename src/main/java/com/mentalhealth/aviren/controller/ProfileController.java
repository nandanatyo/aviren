package com.mentalhealth.aviren.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mentalhealth.aviren.dto.request.UpdateProfileRequest;
import com.mentalhealth.aviren.dto.response.ApiResponse;
import com.mentalhealth.aviren.dto.response.ProfileResponse;
import com.mentalhealth.aviren.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final ProfileService profileService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(Authentication authentication) {
        String email = authentication.getName();
        ProfileResponse response = profileService.getProfile(email);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mendapatkan profil", response));
    }
    
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestPart(value = "profile", required = false) UpdateProfileRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        
        String email = authentication.getName();
        
        if (request == null) {
            request = new UpdateProfileRequest();
        }
        
        ProfileResponse response = profileService.updateProfile(email, request, photo);
        return ResponseEntity.ok(ApiResponse.success("Profil berhasil diupdate", response));
    }
}