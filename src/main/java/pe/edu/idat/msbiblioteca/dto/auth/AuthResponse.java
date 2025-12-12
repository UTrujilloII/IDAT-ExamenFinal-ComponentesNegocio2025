package pe.edu.idat.msbiblioteca.dto.auth;

public record AuthResponse(
        String mensaje,
        String username,
        String role
)
{
}
