package pe.edu.idat.biblioteca.dto.auth;

import java.util.Set;

public record AuthResponse(
        String token,
        String refreshToken,
        String username,
        Set<String> roles
) {}