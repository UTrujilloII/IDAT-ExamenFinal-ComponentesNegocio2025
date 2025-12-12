package pe.edu.idat.biblioteca.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    LibroResponse toResponse (Libro libro);

    Libro toEntity(LibroRequest libroRequest);

    @Mapping(target = "idLibro" ,ignore = true)
    void updateEntityFromRequest(LibroRequest libroRequest, @MappingTarget Libro libro);
}
