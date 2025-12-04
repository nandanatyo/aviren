package com.mentalhealth.aviren.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mentalhealth.aviren.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Page<Chat> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}