package pe.edu.idat.msbiblioteca.dto.auth;

public record RegisterRequest(String username,
                              String password,
                              String role) {
}
