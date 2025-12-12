package pe.edu.idat.biblioteca.mappers;

import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Usuario;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-11T20:46:33-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Set<String> roles = null;
        Long id = null;
        String dni = null;
        String nombre = null;
        String email = null;
        String telefono = null;

        roles = mapRoles( usuario.getRoles() );
        id = usuario.getId();
        dni = usuario.getDni();
        nombre = usuario.getNombre();
        email = usuario.getEmail();
        telefono = usuario.getTelefono();

        UsuarioResponse usuarioResponse = new UsuarioResponse( id, dni, nombre, email, telefono, roles );

        return usuarioResponse;
    }

    @Override
    public Usuario toEntity(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setDni( request.dni() );
        usuario.setTelefono( request.telefono() );
        usuario.setPassword( request.password() );
        usuario.setNombre( request.nombre() );
        usuario.setEmail( request.email() );

        usuario.setEnabled( true );

        return usuario;
    }
}
