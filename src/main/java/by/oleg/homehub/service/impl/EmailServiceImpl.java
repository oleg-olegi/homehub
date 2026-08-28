package by.oleg.homehub.service.impl;

import by.oleg.homehub.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${app.verification-url}")
    private String verificationUrl;
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String email, String token) {
        String verificationLink = verificationUrl + "?token=" + token;
        log.info("Sending verification email to {} with link {}", email, verificationLink);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации HomeHub");
        message.setText("""
                Здравствуйте!
                Для подтверждения регистрации в HomeHub перейдите по ссылке:
                %s
                Ссылка действительна в течение 24 часов.
                Если вы не регистрировались в HomeHub, просто проигнорируйте это письмо.
                """.formatted(verificationLink));
        mailSender.send(message);
    }
}
