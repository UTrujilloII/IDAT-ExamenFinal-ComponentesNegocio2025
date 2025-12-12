package pe.edu.idat.biblioteca.dto.usuario;

import pe.edu.idat.biblioteca.entity.Rol;

import java.time.LocalDate;
import java.util.Set;

public record UsuarioResponse (
        Long idUsuario,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String enabled,
        Set<Rol> roles,
        LocalDate fechaCreacion
){
}
