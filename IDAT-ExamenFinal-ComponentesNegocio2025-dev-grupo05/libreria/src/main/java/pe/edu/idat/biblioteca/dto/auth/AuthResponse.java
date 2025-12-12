package pe.edu.idat.biblioteca.dto.auth;

public record AuthResponse(
        String token,
        String refreshToken,
        String tipo,
        Long id,
        String nombre,
        String apellido,
        String email,
        String rol
) {}