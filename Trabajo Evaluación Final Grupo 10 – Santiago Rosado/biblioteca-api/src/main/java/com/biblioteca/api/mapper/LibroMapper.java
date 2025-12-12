package com.biblioteca.api.mapper;

import com.biblioteca.api.dto.LibroRequestDTO;
import com.biblioteca.api.dto.LibroResponseDTO;
import com.biblioteca.api.model.Libro;

/**
 * ========================================================
 * MAPPER LIBRO
 * --------------------------------------------------------
 * Convierte entidades a DTO y viceversa.
 * Los métodos son estáticos porque no necesitamos crear
 * instancias del mapper. Esto simplifica el código.
 * ========================================================
 */

public class LibroMapper {

    public static LibroResponseDTO toDTO(Libro libro) {
        LibroResponseDTO dto = new LibroResponseDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        dto.setAnioPublicacion(libro.getAnioPublicacion());
        // Agregar si quieres mostrar cantidadDisponible en el response
        return dto;
    }

    public static Libro toEntity(LibroRequestDTO dto) {
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setAnioPublicacion(dto.getAnioPublicacion());
        libro.setCantidadDisponible(dto.getCantidadDisponible());
        return libro;
    }
}