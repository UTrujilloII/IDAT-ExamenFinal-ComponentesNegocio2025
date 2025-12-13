package pe.idat.biblioteca.mappers;

import org.mapstruct.Mapping;
import pe.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.idat.biblioteca.entity.Prestamo;

public interface PrestamoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "devuelto", constant = "false")
    @Mapping(target = "usuario.id", source = "idUsuario")
    @Mapping(target = "libro.id", source = "idLibro")
    Prestamo toEntity(PrestamoRequest prestamoRequest);

    @Mapping(target = "idUsuario", source = "usuario.id")
    @Mapping(target = "nombreUsuario", source = "usuario.nombre")
    @Mapping(target = "idLibro", source = "libro.id")
    @Mapping(target = "tituloLibro", source = "libro.titulo")
    PrestamoResponse toResponse(Prestamo prestamo);
}
