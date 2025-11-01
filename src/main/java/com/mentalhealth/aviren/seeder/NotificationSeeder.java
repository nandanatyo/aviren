package com.mentalhealth.aviren.seeder;

import com.mentalhealth.aviren.entity.Notification;
import com.mentalhealth.aviren.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(5) // Sesuaikan order setelah user seeder
@RequiredArgsConstructor
@Slf4j
public class NotificationSeeder implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

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
        List<Notification> notifications = Arrays.asList(
                // User 1 - Notifikasi
                createNotification(1L, "Pengingat: Meditasi Harian", 
                        "Jangan lupa luangkan 10 menit untuk sesi meditasi harian Anda.", 
                        "REMINDER"),
                createNotification(1L, "Tips Kesehatan: Tetap Terhidrasi", 
                        "Minum air yang cukup sangat penting untuk kesehatan mental dan fisik Anda. Targetkan 8 gelas sehari.", 
                        "HEALTH_TIP"),
                createNotification(1L, "Jadwal Vaksinasi", 
                        "Vaksin flu Anda jatuh tempo minggu depan. Silakan jadwalkan janji temu.", 
                        "VACCINE"),
                
                // User 2 - Notifikasi
                createNotification(2L, "Pengingat: Jalan Sore", 
                        "Waktunya jalan sore! Berjalan 20 menit dapat meningkatkan mood Anda.", 
                        "REMINDER"),
                createNotification(2L, "Tips Kesehatan: Tidur Berkualitas", 
                        "Tidur 7-9 jam sangat penting untuk kesehatan mental. Buat rutinitas tidur yang baik.", 
                        "HEALTH_TIP"),
                
                // User 3 - Notifikasi
                createNotification(3L, "Pengingat: Latihan Pernapasan", 
                        "Luangkan waktu sejenak untuk berlatih pernapasan dalam.", 
                        "REMINDER"),
                createNotification(3L, "Pengingat Vaksin", 
                        "Vaksin booster COVID-19 direkomendasikan. Konsultasikan dengan penyedia layanan kesehatan Anda.", 
                        "VACCINE"),
                createNotification(3L, "Tips Kesehatan: Diet Sehat", 
                        "Diet seimbang yang kaya buah dan sayuran mendukung kesehatan mental Anda.", 
                        "HEALTH_TIP"),
                
                // User 4 - Notifikasi
                createNotification(4L, "Pengingat: Menulis Jurnal", 
                        "Tuliskan pikiran dan perasaan Anda dalam jurnal hari ini.", 
                        "REMINDER"),
                createNotification(4L, "Tips Kesehatan: Koneksi Sosial", 
                        "Tetap terhubung dengan teman dan keluarga. Dukungan sosial sangat penting untuk kesehatan mental.", 
                        "HEALTH_TIP"),
                
                // User 5 - Notifikasi
                createNotification(5L, "Pengingat: Praktik Syukur", 
                        "Tuliskan tiga hal yang Anda syukuri hari ini.", 
                        "REMINDER"),
                createNotification(5L, "Tips Kesehatan: Batasi Waktu Layar", 
                        "Ambil istirahat teratur dari layar untuk mengurangi kelelahan mata dan mental.", 
                        "HEALTH_TIP"),
                createNotification(5L, "Pembaruan Vaksin", 
                        "Vaksin booster tetanus sudah jatuh tempo. Silakan konsultasi dengan dokter Anda.", 
                        "VACCINE"),
                
                // User 1 - Notifikasi tambahan
                createNotification(1L, "Pengingat: Minum Obat", 
                        "Saatnya minum obat pagi Anda. Jangan sampai terlewat!", 
                        "REMINDER"),
                createNotification(1L, "Tips Kesehatan: Olahraga Rutin", 
                        "Olahraga teratur minimal 30 menit sehari dapat meningkatkan kesehatan mental Anda.", 
                        "HEALTH_TIP"),
                
                // User 2 - Notifikasi tambahan
                createNotification(2L, "Pengingat: Konsultasi Psikolog", 
                        "Anda memiliki jadwal konsultasi dengan psikolog besok pukul 14.00.", 
                        "REMINDER"),
                createNotification(2L, "Vaksin Hepatitis B", 
                        "Waktu untuk vaksin Hepatitis B dosis kedua Anda sudah tiba.", 
                        "VACCINE"),
                
                // User 3 - Notifikasi tambahan
                createNotification(3L, "Tips Kesehatan: Manajemen Stres", 
                        "Identifikasi pemicu stres Anda dan cari cara sehat untuk mengatasinya.", 
                        "HEALTH_TIP"),
                createNotification(3L, "Pengingat: Yoga Pagi", 
                        "Mulai hari Anda dengan sesi yoga 15 menit untuk menenangkan pikiran.", 
                        "REMINDER"),
                
                // User 4 - Notifikasi tambahan
                createNotification(4L, "Vaksinasi Anak", 
                        "Jadwal imunisasi anak Anda bulan ini: DPT dan Polio.", 
                        "VACCINE"),
                createNotification(4L, "Tips Kesehatan: Pola Makan Teratur", 
                        "Makan pada waktu yang sama setiap hari membantu mengatur mood dan energi.", 
                        "HEALTH_TIP"),
                
                // User 5 - Notifikasi tambahan
                createNotification(5L, "Pengingat: Batas Waktu Pendaftaran", 
                        "Pendaftaran program kesehatan mental gratis ditutup dalam 3 hari.", 
                        "REMINDER"),
                createNotification(5L, "Tips Kesehatan: Mindfulness", 
                        "Praktikkan mindfulness 5 menit sehari untuk meningkatkan kesadaran diri.", 
                        "HEALTH_TIP")
        );

        notificationRepository.saveAll(notifications);
        log.info("→ Berhasil membuat {} notifikasi", notifications.size());
    }

    private Notification createNotification(Long userId, String title, String content, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        return notification;
    }
}