package by.oleg.homehub.service;

public interface EmailService {
    void sendVerificationEmail(String email, String token);
}
