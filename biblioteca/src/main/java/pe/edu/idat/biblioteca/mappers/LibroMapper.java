package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {
    @Mapping(target = "disponible", expression = "java(libro.getCantidad() > 0 ? \"Sí\" : \"No\")")
    LibroResponse toResponse(Libro libro);

    @Mapping(target = "id", ignore = true)
    Libro toEntity(LibroRequest request);

    @Mapping(target = "id", ignore = true)
    Libro updateEntity(LibroRequest request, @MappingTarget Libro libro);
}