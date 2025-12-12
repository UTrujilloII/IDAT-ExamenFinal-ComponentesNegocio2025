package pe.edu.idat.biblioteca.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    @Mapping(source = "libro.titulo",target = "libro")
    @Mapping(source = "usuario.nombre",target = "usuario")
    PrestamoResponse toResponse(Prestamo prestamo);

    Prestamo toEntity(PrestamoRequest prestamoRequest);
}
