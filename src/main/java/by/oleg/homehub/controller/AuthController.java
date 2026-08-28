package by.oleg.homehub.controller;

import by.oleg.homehub.entity.dto.login.LoginRequestDTO;
import by.oleg.homehub.entity.dto.login.LoginResponseDTO;
import by.oleg.homehub.entity.dto.RegisterRequestDTO;
import by.oleg.homehub.service.RegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO user) {
        log.info("Received request to register user {}", user.email());
        registerService.registerUser(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO login) {
        log.info("Received request to login  {}", login.email());
        LoginResponseDTO token = registerService.login(login);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token) {
        log.info("Received request to verify email {}", token);
        registerService.verifyEmail(token);
        return ResponseEntity.ok("Email successfully verified");
    }
}
