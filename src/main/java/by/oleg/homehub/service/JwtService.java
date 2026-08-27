package by.oleg.homehub.service;

import by.oleg.homehub.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateJwtToken(User user);

    Claims extractAllClaims(String token);
}
