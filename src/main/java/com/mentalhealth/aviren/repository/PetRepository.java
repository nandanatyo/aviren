package com.mentalhealth.aviren.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mentalhealth.aviren.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    Optional<Pet> findByUserId(UUID userId);
}