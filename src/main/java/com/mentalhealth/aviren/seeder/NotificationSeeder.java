package com.mentalhealth.aviren.seeder;

import com.mentalhealth.aviren.entity.Notification;
import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.repository.NotificationRepository;
import com.mentalhealth.aviren.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Order(5) 
@RequiredArgsConstructor
@Slf4j
public class NotificationSeeder implements CommandLineRunner {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (notificationRepository.count() == 0) {
            seedNotifications();
            log.info("✓ Notification seeder berhasil dijalankan");
        } else {
            log.info("⊗ Notifikasi sudah ada, melewati seeder");
        }
    }

    private void seedNotifications() {
        List<User> users = userRepository.findAll();
        
        if (users.isEmpty()) {
            log.warn("⚠ Tidak ada user, tidak bisa membuat notifikasi");
            return;
        }
        
        List<Notification> notifications = new ArrayList<>();
        
        for (int i = 0; i < Math.min(users.size(), 5); i++) {
            User user = users.get(i);
            
            // Notifikasi 1
            notifications.add(createNotification(
                user.getId(), 
                "Pengingat: Meditasi Harian", 
                "Jangan lupa luangkan 10 menit untuk sesi meditasi harian Anda.", 
                "REMINDER"
            ));
            
            // Notifikasi 2
            notifications.add(createNotification(
                user.getId(), 
                "Tips Kesehatan: Tetap Terhidrasi", 
                "Minum air yang cukup sangat penting untuk kesehatan mental dan fisik Anda. Targetkan 8 gelas sehari.", 
                "HEALTH_TIP"
            ));
            
            // Notifikasi 3 (hanya untuk beberapa user)
            if (i % 2 == 0) {
                notifications.add(createNotification(
                    user.getId(), 
                    "Jadwal Vaksinasi", 
                    "Vaksin flu Anda jatuh tempo minggu depan. Silakan jadwalkan janji temu.", 
                    "VACCINE"
                ));
            }
        }

        notificationRepository.saveAll(notifications);
        log.info("→ Berhasil membuat {} notifikasi", notifications.size());
    }

    private Notification createNotification(UUID userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        return notification;
    }
}