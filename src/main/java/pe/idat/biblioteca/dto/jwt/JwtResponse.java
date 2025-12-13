package pe.idat.biblioteca.dto.jwt;

public record JwtResponse(String accessToken,
                          String refreshToken,
                          String username,
                          String role) {
}
