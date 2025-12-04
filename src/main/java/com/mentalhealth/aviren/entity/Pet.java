package com.mentalhealth.aviren.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "animal_type", nullable = false, length = 50)
    private String animalType;
    
    @Column(nullable = false, length = 10)
    private String gender;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(nullable = false)
    private Boolean vaccine;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "pet_home_image", length = 500)
    private String petHomeImage;
    
    @Column(name = "pet_profile_image", length = 500)
    private String petProfileImage;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}