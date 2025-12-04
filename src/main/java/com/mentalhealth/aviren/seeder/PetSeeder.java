package com.mentalhealth.aviren.seeder;

import com.mentalhealth.aviren.entity.Pet;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.repository.PetRepository;
import com.mentalhealth.aviren.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Order(3)
@RequiredArgsConstructor
@Slf4j
public class PetSeeder implements CommandLineRunner {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
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
        List<User> users = userRepository.findAll();
        
        if (users.isEmpty()) {
            log.warn("⚠ Tidak ada user, tidak bisa membuat pet");
            return;
        }
        
        List<Pet> pets = new ArrayList<>();
        
        String[] names = {"Benji", "Bailey", "Bruno", "Bella", "Buddy"};
        String[] genders = {"Male", "Female"};
        
        for (int i = 0; i < Math.min(users.size(), 5); i++) {
            User user = users.get(i);
            String name = names[i % names.length];
            String gender = genders[i % 2];
            LocalDate birthDate = LocalDate.now().minusYears(2 + i);
            double weight = 20.0 + (i * 2.5);
            boolean vaccine = i % 2 == 0;
            
            Pet pet = createPet(
                user.getId(), 
                name, 
                gender, 
                birthDate, 
                weight, 
                vaccine,
                String.format("%s adalah seekor beaver yang ramah dan suka menemani. %s siap menjadi teman curhatmu!", name, name)
            );
            
            pets.add(pet);
        }

        petRepository.saveAll(pets);
        log.info("→ Berhasil membuat {} pets", pets.size());
    }

    private Pet createPet(UUID userId, String name, String gender, LocalDate birthDate, 
                         Double weight, Boolean vaccine, String description) {
        Pet pet = new Pet();
        pet.setUserId(userId);
        pet.setName(name);
        pet.setAnimalType("Beaver");
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