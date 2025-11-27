package com.mentalhealth.aviren.seeder;

import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Order(3)
@RequiredArgsConstructor
@Slf4j
public class PetSeeder implements CommandLineRunner {

    private final PetRepository petRepository;
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (petRepository.count() == 0) {
            seedPets();
            log.info("✓ Pet seeder berhasil dijalankan");
        } else {
            log.info("⊗ Pet sudah ada, melewati seeder");
        }
    }

    private void seedPets() {
        List<Pet> pets = Arrays.asList(
                createPet(1L, "Benji", "Male", LocalDate.of(2020, 3, 15), 25.50, true,
                        "Benji adalah seekor beaver yang ramah dan suka menemani. Benji siap menjadi teman curhatmu!"),
                
                createPet(2L, "Bailey", "Female", LocalDate.of(2019, 7, 22), 22.30, true,
                        "Bailey adalah seekor beaver yang ceria dan penuh empati. Bailey siap menjadi teman curhatmu!"),
                
                createPet(3L, "Bruno", "Male", LocalDate.of(2021, 1, 10), 28.75, false,
                        "Bruno adalah seekor beaver yang penyayang dan pengertian. Bruno siap menjadi teman curhatmu!"),
                
                createPet(4L, "Bella", "Female", LocalDate.of(2020, 11, 5), 20.80, true,
                        "Bella adalah seekor beaver yang baik hati dan penuh perhatian. Bella siap menjadi teman curhatmu!"),
                
                createPet(5L, "Buddy", "Male", LocalDate.of(2018, 9, 18), 30.20, true,
                        "Buddy adalah seekor beaver yang setia dan suportif. Buddy siap menjadi teman curhatmu!")
        );

        petRepository.saveAll(pets);
        log.info("→ Berhasil membuat {} pets", pets.size());
    }

    private Pet createPet(Long userId, String name, String gender, LocalDate birthDate, 
                         Double weight, Boolean vaccine, String description) {
        Pet pet = new Pet();
        pet.setUserId(userId);
        pet.setName(name);
        pet.setAnimalType("Beaver"); // Always Beaver for now
        pet.setGender(gender);
        pet.setBirthDate(birthDate);
        pet.setWeight(BigDecimal.valueOf(weight));
        pet.setVaccine(vaccine);
        pet.setDescription(description);
        
        pet.setPetHomeImage("pet-images/beaver-home.png");
        pet.setPetProfileImage("pet-images/beaver-profile.png");
        
        return pet;
    }
}