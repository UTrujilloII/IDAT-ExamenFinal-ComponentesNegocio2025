package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.libro.LibroDTO;
import com.biblioteca.biblioteca_api.dtos.libro.LibroResponseDTO;
import com.biblioteca.biblioteca_api.entity.Libro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-30T23:35:01-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class LibroMapperImpl implements LibroMapper {

    @Override
    public Libro toEntity(LibroDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Libro.LibroBuilder libro = Libro.builder();

        libro.titulo( dto.getTitulo() );
        libro.autor( dto.getAutor() );
        libro.isbn( dto.getIsbn() );
        libro.totalEjemplares( dto.getTotalEjemplares() );
        libro.disponibles( dto.getDisponibles() );

        return libro.build();
    }

    @Override
    public LibroResponseDTO toResponseDTO(Libro libro) {
        if ( libro == null ) {
            return null;
        }

        LibroResponseDTO.LibroResponseDTOBuilder libroResponseDTO = LibroResponseDTO.builder();

        libroResponseDTO.id( libro.getId() );
        libroResponseDTO.titulo( libro.getTitulo() );
        libroResponseDTO.autor( libro.getAutor() );
        libroResponseDTO.isbn( libro.getIsbn() );
        libroResponseDTO.totalEjemplares( libro.getTotalEjemplares() );
        libroResponseDTO.disponibles( libro.getDisponibles() );
        if ( libro.getActivo() != null ) {
            libroResponseDTO.activo( String.valueOf( libro.getActivo() ) );
        }

        return libroResponseDTO.build();
    }
}
