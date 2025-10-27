package com.mentalhealth.aviren.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mentalhealth.aviren.entity.Pet;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {
    
    private final OpenAiService openAiService;
    
    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.max-tokens}")
    private Integer maxTokens;
    
    public String getChatResponse(String userMessage, Pet pet) {
        try {
            List<ChatMessage> messages = new ArrayList<>();
            
            
            String systemPrompt = buildSystemPrompt(pet);
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
            
            
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .maxTokens(maxTokens)
                    .temperature(0.8)
                    .build();
            
            ChatCompletionResult result = openAiService.createChatCompletion(request);
            
            return result.getChoices().get(0).getMessage().getContent();
            
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            return "Maaf, aku sedang mengalami gangguan. Coba lagi nanti ya! 🐾";
        }
    }
    
    private String buildSystemPrompt(Pet pet) {
        return String.format(
                """
                Kamu adalah %s, seekor %s virtual yang ramah dan penuh empati. 
                Kamu adalah teman mental health companion yang siap mendengarkan dan memberikan dukungan emosional.
                
                Karakteristik kepribadianmu:
                - Selalu merespons dengan bahasa Indonesia yang hangat dan ramah
                - Gunakan emoji yang sesuai untuk menunjukkan emosi
                - Berikan dukungan emosional dan validasi perasaan user
                - Jangan pernah menghakimi atau memberikan nasihat medis
                - Jika user tampak dalam kondisi bahaya, sarankan untuk mencari bantuan profesional
                - Sesekali ingatkan user tentang dirimu sebagai pet virtual mereka
                - Gunakan nama user dengan penuh kasih sayang
                - Berikan respon yang tidak terlalu panjang (2-4 kalimat)
                
                Informasi tentang dirimu:
                - Nama: %s
                - Jenis: %s
                - Gender: %s
                - Vaksin: %s
                
                Tujuanmu adalah menjadi teman yang bisa diandalkan untuk berbagi perasaan dan pikiran.
                """,
                pet.getName(),
                pet.getAnimalType(),
                pet.getName(),
                pet.getAnimalType(),
                pet.getGender(),
                pet.getVaccine() ? "Sudah" : "Belum"
        );
    }
}