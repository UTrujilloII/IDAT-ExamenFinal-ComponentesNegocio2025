package pe.edu.idat.biblioteca.dto;

import java.util.Set;

public record UsuarioAdminResponse(
        Long idUsuario,
        String nombre,
        String apellido,
        String telefono,
        String email,
        Set<String> roles
) {
}
