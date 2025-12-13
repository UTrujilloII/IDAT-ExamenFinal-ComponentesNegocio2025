package pe.edu.idat.biblioteca.mappers;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.Usuario;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T19:40:11-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        String rol = null;
        Long id = null;
        String nombre = null;
        String apellido = null;
        String email = null;
        Boolean activo = null;
        LocalDateTime fechaRegistro = null;

        rol = usuarioRolNombre( usuario );
        id = usuario.getId();
        nombre = usuario.getNombre();
        apellido = usuario.getApellido();
        email = usuario.getEmail();
        activo = usuario.getActivo();
        fechaRegistro = usuario.getFechaRegistro();

        UsuarioResponse usuarioResponse = new UsuarioResponse( id, nombre, apellido, email, activo, rol, fechaRegistro );

        return usuarioResponse;
    }

    @Override
    public Usuario toEntity(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNombre( request.nombre() );
        usuario.setApellido( request.apellido() );
        usuario.setEmail( request.email() );

        return usuario;
    }

    private String usuarioRolNombre(Usuario usuario) {
        Rol rol = usuario.getRol();
        if ( rol == null ) {
            return null;
        }
        return rol.getNombre();
    }
}
