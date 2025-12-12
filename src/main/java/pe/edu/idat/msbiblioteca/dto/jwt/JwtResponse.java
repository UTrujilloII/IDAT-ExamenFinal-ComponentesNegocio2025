package pe.edu.idat.msbiblioteca.dto.jwt;

public record JwtResponse(
        String accessToken,
        String refreshToken,
        String username,
        String role
) {
}
