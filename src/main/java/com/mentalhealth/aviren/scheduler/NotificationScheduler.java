package com.mentalhealth.aviren.scheduler;

import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mentalhealth.aviren.entity.User;
import com.mentalhealth.aviren.repository.UserRepository;
import com.mentalhealth.aviren.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final Random random = new Random();
    
    
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyReminder() {
        log.info("Sending daily reminder notifications...");
        
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            String[] messages = {
                    "Hai! Pet kamu kangen nih, yuk ngobrol sebentar! 🐾",
                    "Sudah waktunya curhat dengan pet kesayanganmu! 💙",
                    "Jangan lupa berbagi cerita hari ini dengan pet kamu ya! 🌟"
            };
            
            String message = messages[random.nextInt(messages.length)];
            notificationService.createNotification(
                    user.getId(),
                    "Waktunya Chat!",
                    message,
                    "REMINDER"
            );
        }
        
        log.info("Daily reminder sent to {} users", users.size());
    }
    
    
    @Scheduled(cron = "0 0 19 * * ?")
    public void sendHealthTip() {
        log.info("Sending health tip notifications...");
        
        List<User> users = userRepository.findAll();
        
        String[] tips = {
                "Jangan lupa untuk istirahat yang cukup malam ini. Tidur 7-8 jam sangat penting untuk kesehatan mentalmu! 😴",
                "Cobalah untuk bernapas dalam-dalam 5 kali. Ini bisa membantu menenangkan pikiran. 🧘",
                "Luangkan waktu 10 menit untuk melakukan hal yang kamu sukai hari ini! 🎨",
                "Jangan lupa minum air putih yang cukup. Hidrasi yang baik membantu mood yang lebih baik! 💧",
                "Coba tulis 3 hal yang kamu syukuri hari ini. Gratitude meningkatkan kebahagiaan! 📝"
        };
        
        for (User user : users) {
            String tip = tips[random.nextInt(tips.length)];
            notificationService.createNotification(
                    user.getId(),
                    "Tips Kesehatan Mental",
                    tip,
                    "HEALTH_TIP"
            );
        }
        
        log.info("Health tips sent to {} users", users.size());
    }
    
    
    @Scheduled(cron = "0 0 10 ? * MON")
    public void sendVaccineReminder() {
        log.info("Sending vaccine reminder notifications...");
        
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            notificationService.createNotification(
                    user.getId(),
                    "Reminder Vaksin Pet",
                    "Jangan lupa cek jadwal vaksin pet virtualmu! Kesehatan pet adalah prioritas! 💉",
                    "VACCINE"
            );
        }
        
        log.info("Vaccine reminders sent to {} users", users.size());
    }
}