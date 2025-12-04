package com.mentalhealth.aviren.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalhealth.aviren.dto.request.SendChatRequest;
import com.mentalhealth.aviren.dto.response.ChatResponse;
import com.mentalhealth.aviren.entity.Chat;
import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.exception.ResourceNotFoundException;
import com.mentalhealth.aviren.repository.ChatRepository;
import com.mentalhealth.aviren.repository.PetRepository;
import com.mentalhealth.aviren.repository.UserRepository;
import java.util.Collections;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final OpenAIService openAIService;
    
    public List<ChatResponse> getChatHistory(String email, int page, int size) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chats = chatRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        
        return chats.getContent().stream()
                .map(this::mapToChatResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ChatResponse sendMessage(String email, SendChatRequest request) {
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));
        
        
        Pet pet = petRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet tidak ditemukan"));
        
       Pageable pageable = PageRequest.of(0, 5);
Page<Chat> recentChats = chatRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

// FIX: ubah ke list mutable sebelum reverse
List<Chat> chatHistory = new java.util.ArrayList<>(recentChats.getContent());
Collections.reverse(chatHistory);
        
        
        Chat userChat = new Chat();
        userChat.setUserId(user.getId());
        userChat.setPetId(pet.getId());
        userChat.setMessage(request.getMessage());
        userChat.setSenderType("USER");
        chatRepository.save(userChat);
        
        
        String aiResponse = openAIService.getChatResponse(request.getMessage(), pet, chatHistory);
        
        
        Chat petChat = new Chat();
        petChat.setUserId(user.getId());
        petChat.setPetId(pet.getId());
        petChat.setMessage(aiResponse);
        petChat.setSenderType("PET");
        Chat savedPetChat = chatRepository.save(petChat);
        
        return mapToChatResponse(savedPetChat);
    }
    
    private ChatResponse mapToChatResponse(Chat chat) {
        return new ChatResponse(
                chat.getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt()
        );
    }
}