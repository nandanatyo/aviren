package com.mentalhealth.aviren.enums;

import java.util.Random;

public enum WelcomingStatement {
    GREETING_1("Halo! Senang bertemu denganmu hari ini! 🦫"),
    GREETING_2("Hai! Aku siap mendengarkan ceritamu! 💙"),
    GREETING_3("Selamat datang kembali! Bagaimana kabarmu? 🌟"),
    GREETING_4("Hai teman! Ada yang ingin kamu ceritakan? 🐾"),
    GREETING_5("Halo! Aku kangen nih, yuk ngobrol! ✨"),
    GREETING_6("Hai! Gimana harimu? Cerita dong! 🦫💕"),
    GREETING_7("Selamat datang! Aku di sini untukmu! 🌈"),
    GREETING_8("Halo sahabat! Yuk berbagi cerita! 🎈"),
    GREETING_9("Hai! Aku senang kamu datang! 🦫"),
    GREETING_10("Selamat datang! Ada yang bisa aku bantu? 💙"),
    GREETING_11("Halo! Ingat ya, kamu sudah hebat sejauh ini. Lanjutkan! ✨"),
    GREETING_12("Hai! Jangan lupa tarik napas dalam… kamu aman, kamu mampu. 💙"),
    GREETING_13("Selamat datang! Hari ini kamu berhak merasa bangga pada dirimu. 🌿"),
    GREETING_14("Hai! Pelan-pelan aja ya, kamu nggak harus sempurna kok. 🌈"),
    GREETING_15("Halo! Satu langkah kecil tetap langkah maju. Aku di sini temenin. 🦫"),
    GREETING_16("Hai! Semoga hari ini kamu menemukan ketenangan yang kamu butuhkan. ☀️"),
    GREETING_17("Selamat datang! Kamu berhak istirahat dan merasa lebih baik. 💛");
    
    private final String message;
    private static final Random random = new Random();
    
    WelcomingStatement(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public static String getRandomMessage() {
        WelcomingStatement[] statements = values();
        return statements[random.nextInt(statements.length)].getMessage();
    }
}