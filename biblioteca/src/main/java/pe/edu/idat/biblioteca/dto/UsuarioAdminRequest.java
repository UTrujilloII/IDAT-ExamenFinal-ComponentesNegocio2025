package pe.edu.idat.biblioteca.dto;

import java.util.Set;

public record UsuarioAdminRequest (
        String nombre,
        String apellido,
        String telefono,
        String email,
        String password,
        Set<Long> rolesIds
) {
}
