package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.prestamo.PrestamoResponse;
import com.biblioteca.biblioteca_api.entity.Prestamo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    @Mapping(target = "usuario", expression = "java(prestamo.getUsuario().getNombre())")
    @Mapping(target = "libro", expression = "java(prestamo.getLibro().getTitulo())")
    @Mapping(target = "estado", expression = "java(prestamo.getEstado().name())")
    PrestamoResponse toResponseDTO(Prestamo prestamo);
}
