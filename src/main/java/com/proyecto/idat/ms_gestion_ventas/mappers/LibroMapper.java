package com.proyecto.idat.ms_gestion_ventas.mappers;

import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    @Mapping(target = "idLibro", ignore = true)
    @Mapping(target = "ejemplaresDisponibles", ignore = true) // se setea en la logica
    @Mapping(target = "prestamos", ignore = true)             // no viene en el request
    @Mapping(target = "categoria", ignore = true)             // la setea el service
    Libro toEntity(LibroRequest request);

    @Mapping(target = "idLibro", source = "idLibro")          // explicito para que no avise
    @Mapping(target = "ejemplaresDisponibles", source = "ejemplaresDisponibles")
    @Mapping(target = "categoriaId", source = "categoria.idCategoria")
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    LibroResponse toResponse(Libro libro);

    // Actualizar entidad existente con datos del request
    @Mapping(target = "idLibro", ignore = true)                // el id no se toca
    @Mapping(target = "ejemplaresDisponibles", ignore = true)  // se recalculas en el service
    @Mapping(target = "prestamos", ignore = true)
    @Mapping(target = "categoria", ignore = true) // la cambia el service
    void updateFromRequest(LibroRequest request, @MappingTarget Libro libro);
}
