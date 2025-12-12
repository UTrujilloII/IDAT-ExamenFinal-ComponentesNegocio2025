package pe.edu.idat.biblioteca.dto.auth;

public record AuthResponse (
        String mensaje,
        String email,
        String roles
){
}
