package com.mentalhealth.aviren.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mentalhealth.aviren.dto.request.UpdateProfileRequest;
import com.mentalhealth.aviren.dto.response.PetResponse;
import com.mentalhealth.aviren.dto.response.ProfileResponse;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.exception.BadRequestException;
import com.mentalhealth.aviren.exception.ResourceNotFoundException;
import com.mentalhealth.aviren.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final UserRepository userRepository;
    private final PetService petService;
    private final MinioService minioService;
    
    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        PetResponse petResponse = petService.getPetByUserId(user.getId());
        
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfilePhoto(),
                petResponse
        );
    }
    
    @Transactional
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request, MultipartFile photo) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        
        
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail()) && 
                userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email sudah digunakan");
            }
            user.setEmail(request.getEmail());
        }
        
        
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = minioService.uploadFile(photo, "profile-photos");
            user.setProfilePhoto(photoUrl);
        }
        
        User updatedUser = userRepository.save(user);
        
        PetResponse petResponse = petService.getPetByUserId(user.getId());

        return new ProfileResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getProfilePhoto(),
                petResponse
        );
    }
}