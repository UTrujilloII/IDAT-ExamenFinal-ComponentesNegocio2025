package pe.edu.idat.biblioteca.dto.jwt;

public record JwtResponse(
        String token,
        String refreshToken
) {}
