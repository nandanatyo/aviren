package com.mentalhealth.aviren.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mentalhealth.aviren.dto.response.PetResponse;
import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.enums.WelcomingStatement;
import com.mentalhealth.aviren.exception.ResourceNotFoundException;
import com.mentalhealth.aviren.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    
    private final PetRepository petRepository;
    private final MinioService minioService;
    
    @Value("${server.base-url}")
    private String serverBaseUrl;
    
    @Value("${pet.default.min-weight}")
    private Double minWeight;
    
    @Value("${pet.default.max-weight}")
    private Double maxWeight;
    
    @Value("${pet.default.min-age-years}")
    private Integer minAgeYears;
    
    @Value("${pet.default.max-age-years}")
    private Integer maxAgeYears;
    
    private final Random random = new Random();
    
    public Pet createDefaultPetForUser(UUID userId) {
        Pet pet = new Pet();
        pet.setUserId(userId);
        
        String animalType = "Beaver";
        pet.setAnimalType(animalType);
        
        pet.setName(generateRandomPetName());
        pet.setGender(getRandomGender());
        pet.setBirthDate(generateRandomBirthDate());
        pet.setWeight(generateRandomWeight());
        pet.setVaccine(random.nextBoolean());
        pet.setDescription(generatePetDescription(pet));
        
        assignPetImages(pet, animalType);
        
        return petRepository.save(pet);
    }
    
    public PetResponse getPetByUserId(UUID userId) {
        Pet pet = petRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet tidak ditemukan untuk user ini"));
        
        return mapToPetResponse(pet);
    }
    
    private void assignPetImages(Pet pet, String animalType) {
        switch (animalType.toLowerCase()) {
            case "beaver":
                pet.setPetHomeImage("pet-images/beaver-home.png");
                pet.setPetProfileImage("pet-images/beaver-profile.png");
                break;
            default:
                pet.setPetHomeImage("pet-images/default-home.png");
                pet.setPetProfileImage("pet-images/default-profile.png");
        }
    }
    
    private String generateRandomPetName() {
        String[] names = {
            "Benji", "Bailey", "Bucky", "Bruno", "Barry", 
            "Betty", "Bella", "Bonnie", "Buddy", "Brownie",
            "Bernie", "Benson", "Bentley", "Biscuit", "Buttons"
        };
        return names[random.nextInt(names.length)];
    }
    
    private String getRandomGender() {
        return random.nextBoolean() ? "Male" : "Female";
    }
    
    private LocalDate generateRandomBirthDate() {
        int ageInYears = minAgeYears + random.nextInt(maxAgeYears - minAgeYears + 1);
        int daysToSubtract = ageInYears * 365 + random.nextInt(365);
        return LocalDate.now().minusDays(daysToSubtract);
    }
    
    private BigDecimal generateRandomWeight() {
        double weight = minWeight + (maxWeight - minWeight) * random.nextDouble();
        return BigDecimal.valueOf(Math.round(weight * 100.0) / 100.0);
    }
    
    private String generatePetDescription(Pet pet) {
        return String.format("%s adalah seekor %s yang ramah dan suka menemani. %s siap menjadi teman curhatmu!",
                pet.getName(), pet.getAnimalType().toLowerCase(), 
                pet.getName());
    }
    
    private PetResponse mapToPetResponse(Pet pet) {
        String petHomeImageUrl = minioService.generateFileUrl(pet.getPetHomeImage(), serverBaseUrl);
        String petProfileImageUrl = minioService.generateFileUrl(pet.getPetProfileImage(), serverBaseUrl);
        
        String welcomingStatement = WelcomingStatement.getRandomMessage();
        
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getAnimalType(),
                pet.getGender(),
                pet.getBirthDate(),
                pet.getWeight(),
                pet.getVaccine(),
                pet.getDescription(),
                petHomeImageUrl,
                petProfileImageUrl,
                welcomingStatement
        );
    }
}