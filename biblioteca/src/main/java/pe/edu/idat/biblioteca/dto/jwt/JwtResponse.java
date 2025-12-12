package pe.edu.idat.biblioteca.dto.jwt;

public record JwtResponse(
        String accessToken,
        String RefreshToken,
        String email,
        String role

) {
}
