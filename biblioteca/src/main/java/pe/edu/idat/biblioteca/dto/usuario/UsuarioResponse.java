package pe.edu.idat.biblioteca.dto.usuario;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String dni,          // <--- CAMPO AÑADIDO
        String nombre,
        String email,
        String telefono,     // <--- CAMPO AÑADIDO
        Set<String> roles
) {}