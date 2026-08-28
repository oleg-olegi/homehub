package by.oleg.homehub.service.impl;

import by.oleg.homehub.entity.EmailVerificationToken;
import by.oleg.homehub.entity.User;
import by.oleg.homehub.repository.EmailVerificationTokenRepository;
import by.oleg.homehub.repository.UserRepository;
import by.oleg.homehub.service.EmailService;
import by.oleg.homehub.service.EmailVerificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public void createAndSendVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(emailVerificationToken);

        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }
        User user = verificationToken.getUser();

        user.setEmailVerified(true);

        userRepository.save(user);

        tokenRepository.delete(verificationToken);
        log.info("Email verification - successfully");
    }
}
