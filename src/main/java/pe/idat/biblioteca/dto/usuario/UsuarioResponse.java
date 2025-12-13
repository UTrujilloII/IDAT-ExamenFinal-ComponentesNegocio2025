package pe.idat.biblioteca.dto.usuario;

import java.util.Set;

public record UsuarioResponse(
                              Long id,
                              String username,
                              String email,
                              boolean enabled,
                              Set<String> roles) {
}
