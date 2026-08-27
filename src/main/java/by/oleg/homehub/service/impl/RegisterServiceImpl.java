package by.oleg.homehub.service.impl;

import by.oleg.homehub.entity.User;
import by.oleg.homehub.entity.dto.login.LoginRequestDTO;
import by.oleg.homehub.entity.dto.login.LoginResponseDTO;
import by.oleg.homehub.entity.dto.RegisterRequestDTO;
import by.oleg.homehub.entity.enums.Role;
import by.oleg.homehub.repository.UserRepository;
import by.oleg.homehub.service.JwtService;
import by.oleg.homehub.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void registerUser(RegisterRequestDTO request) {
        validateNameAndEmail(request);
        User user = new User();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO user) {
        User foundUser = userRepository.findByEmail(user.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(user.password(), foundUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return new LoginResponseDTO(jwtService.generateJwtToken(foundUser));
    }

    private void validateNameAndEmail(RegisterRequestDTO request) {
        log.info("validateNameAndEmail");
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
    }
}
