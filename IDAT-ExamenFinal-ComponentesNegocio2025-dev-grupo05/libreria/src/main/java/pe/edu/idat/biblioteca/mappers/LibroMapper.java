package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    @Mapping(target = "cantidadDisponible", source = "cantidadTotal")

    Libro toEntity(LibroRequest request);

    LibroResponse toResponse(Libro libro);
}