package com.mentalhealth.aviren.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mentalhealth.aviren.dto.response.PetResponse;
import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.exception.ResourceNotFoundException;
import com.mentalhealth.aviren.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    
    private final PetRepository petRepository;
    
    @Value("${pet.default.animal-types}")
    private String animalTypes;
    
    @Value("${pet.default.min-weight}")
    private Double minWeight;
    
    @Value("${pet.default.max-weight}")
    private Double maxWeight;
    
    @Value("${pet.default.min-age-years}")
    private Integer minAgeYears;
    
    @Value("${pet.default.max-age-years}")
    private Integer maxAgeYears;
    
    private final Random random = new Random();
    
    public Pet createDefaultPetForUser(Long userId) {
        Pet pet = new Pet();
        pet.setUserId(userId);
        pet.setName(generateRandomPetName());
        pet.setAnimalType(getRandomAnimalType());
        pet.setGender(getRandomGender());
        pet.setBirthDate(generateRandomBirthDate());
        pet.setWeight(generateRandomWeight());
        pet.setVaccine(random.nextBoolean());
        pet.setDescription(generatePetDescription(pet));
        
        return petRepository.save(pet);
    }
    
    public PetResponse getPetByUserId(Long userId) {
        Pet pet = petRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet tidak ditemukan untuk user ini"));
        
        return mapToPetResponse(pet);
    }
    
    private String generateRandomPetName() {
        String[] names = {
            "Mochi", "Luna", "Coco", "Bella", "Max", "Charlie", 
            "Lucy", "Buddy", "Daisy", "Rocky", "Molly", "Bailey",
            "Simba", "Nala", "Leo", "Oscar", "Kiki", "Lilo"
        };
        return names[random.nextInt(names.length)];
    }
    
    private String getRandomAnimalType() {
        String[] types = animalTypes.split(",");
        return types[random.nextInt(types.length)].trim();
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
        String pronoun = pet.getGender().equals("Male") ? "dia" : "dia";
        return String.format("%s adalah seekor %s yang ramah dan suka menemani. %s siap menjadi teman curhatmu!",
                pet.getName(), pet.getAnimalType().toLowerCase(), 
                pet.getName());
    }
    
    private PetResponse mapToPetResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getAnimalType(),
                pet.getGender(),
                pet.getBirthDate(),
                pet.getWeight(),
                pet.getVaccine(),
                pet.getDescription()
        );
    }
}