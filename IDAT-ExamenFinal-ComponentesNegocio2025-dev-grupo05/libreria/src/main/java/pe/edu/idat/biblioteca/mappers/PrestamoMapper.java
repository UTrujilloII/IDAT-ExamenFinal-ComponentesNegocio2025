package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(expression = "java(prestamo.getUsuario().getNombre() + \" \" + prestamo.getUsuario().getApellido())", target = "nombreUsuario")
    @Mapping(source = "libro.id", target = "libroId")
    @Mapping(source = "libro.titulo", target = "tituloLibro")
    PrestamoResponse toResponse(Prestamo prestamo);

    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(source = "libroId", target = "libro.id")
    Prestamo toEntity(PrestamoRequest request);
}