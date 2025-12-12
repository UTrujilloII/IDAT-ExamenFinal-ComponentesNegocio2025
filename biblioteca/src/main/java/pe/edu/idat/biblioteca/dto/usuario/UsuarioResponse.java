package pe.edu.idat.biblioteca.dto.usuario;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String dni,
        String nombre,
        String email,
        String telefono,
        Set<String> roles
) {}