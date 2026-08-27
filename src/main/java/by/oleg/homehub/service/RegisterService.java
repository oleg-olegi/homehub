package by.oleg.homehub.service;

import by.oleg.homehub.entity.dto.login.LoginRequestDTO;
import by.oleg.homehub.entity.dto.login.LoginResponseDTO;
import by.oleg.homehub.entity.dto.RegisterRequestDTO;

public interface RegisterService {
    void registerUser(RegisterRequestDTO user);

    LoginResponseDTO login(LoginRequestDTO user);
}
