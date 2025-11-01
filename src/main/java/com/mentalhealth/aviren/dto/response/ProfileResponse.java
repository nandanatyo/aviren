package com.mentalhealth.aviren.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String profilePhoto;
    private PetResponse pet;
}