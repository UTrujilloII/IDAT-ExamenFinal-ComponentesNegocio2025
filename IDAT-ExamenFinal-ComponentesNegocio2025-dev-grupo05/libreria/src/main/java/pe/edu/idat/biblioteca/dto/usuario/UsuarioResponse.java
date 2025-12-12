package pe.edu.idat.biblioteca.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        Boolean activo,
        String rol,
        LocalDateTime fechaRegistro
) {}