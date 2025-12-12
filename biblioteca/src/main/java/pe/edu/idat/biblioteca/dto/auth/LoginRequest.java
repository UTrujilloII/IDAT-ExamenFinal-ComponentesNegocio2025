package pe.edu.idat.biblioteca.dto.auth;

public record LoginRequest (
        String email,
        String password
){
}
