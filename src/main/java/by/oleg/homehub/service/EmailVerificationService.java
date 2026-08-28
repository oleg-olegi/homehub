package by.oleg.homehub.service;

import by.oleg.homehub.entity.User;

public interface EmailVerificationService {
    void createAndSendVerificationToken(User user);

    void verifyEmail(String token);
}
