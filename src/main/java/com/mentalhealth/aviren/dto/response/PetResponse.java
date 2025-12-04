package com.mentalhealth.aviren.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private UUID id;
    private String name;
    private String animalType;
    private String gender;
    private LocalDate birthDate;
    private BigDecimal weight;
    private Boolean vaccine;
    private String description;
    private String petHomeImage;
    private String petProfileImage;
    private String welcomingStatement;
}