package pe.edu.idat.biblioteca.mapper;

import org.mapstruct.Mapper;
import pe.edu.idat.biblioteca.dto.UsuarioAdminRequest;
import pe.edu.idat.biblioteca.dto.UsuarioAdminResponse;
import pe.edu.idat.biblioteca.entity.Usuario;



@Mapper(componentModel = "spring")
public interface UsuarioAdminMapper {

//    UsuarioAdminResponse toResponse(Usuario usuario);


    Usuario toEntity(UsuarioAdminRequest usuarioAdminRequest);
}
