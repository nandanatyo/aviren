package com.mentalhealth.aviren.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalhealth.aviren.dto.request.LoginRequest;
import com.mentalhealth.aviren.dto.request.RegisterRequest;
import com.mentalhealth.aviren.dto.response.AuthResponse;
import com.mentalhealth.aviren.dto.response.PetResponse;
import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.exception.BadRequestException;
import com.mentalhealth.aviren.repository.UserRepository;
import com.mentalhealth.aviren.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PetService petService;
    private final MinioService minioService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Value("${server.base-url:http://localhost:8080}")
    private String serverBaseUrl;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email sudah terdaftar");
        }
        
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        
        Pet pet = petService.createDefaultPetForUser(savedUser.getId());
        
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        
        // Generate dynamic URL
        String profilePhotoUrl = minioService.generateFileUrl(savedUser.getProfilePhoto(), serverBaseUrl);
        
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                profilePhotoUrl
        );
        
        PetResponse petResponse = new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getAnimalType(),
                pet.getGender(),
                pet.getBirthDate(),
                pet.getWeight(),
                pet.getVaccine(),
                pet.getDescription()
        );
        
        return new AuthResponse(token, "Bearer", userInfo, petResponse);
    }
    
    public AuthResponse login(LoginRequest request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Email atau password salah"));
        
        
        PetResponse petResponse = petService.getPetByUserId(user.getId());
        
        // Generate dynamic URL
        String profilePhotoUrl = minioService.generateFileUrl(user.getProfilePhoto(), serverBaseUrl);
        
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getName(),
                user.getEmail(),
                profilePhotoUrl
        );
        
        return new AuthResponse(token, "Bearer", userInfo, petResponse);
    }
}