package com.biblioteca.biblioteca_api.dtos.libro;

import com.biblioteca.biblioteca_api.entity.Libro;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LibroResponseDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer totalEjemplares;
    private Integer disponibles;
    private String activo;


    // Convierte entidad a DTO
    public static LibroResponseDTO fromEntity(Libro libro) {
        return LibroResponseDTO.builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .isbn(libro.getIsbn())
                .totalEjemplares(libro.getTotalEjemplares())
                .disponibles(libro.getDisponibles())
                .activo(libro.getActivo() ? "si esta activo" : "esta inactivo lo siento")
                .build();
    }
}
