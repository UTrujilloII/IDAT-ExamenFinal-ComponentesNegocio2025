package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.libro.LibroDTO;
import com.biblioteca.biblioteca_api.dtos.libro.LibroResponseDTO;
import com.biblioteca.biblioteca_api.entity.Libro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    Libro toEntity(LibroDTO dto);

    LibroResponseDTO toResponseDTO(Libro libro);
}
