package com.biblioteca.biblioteca_api.dtos.libro;

import com.biblioteca.biblioteca_api.entity.Libro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibroDTO {

    @NotBlank
    private String titulo;

    @NotBlank
    private String autor;

    @NotBlank
    private String isbn;

    @NotNull
    private Integer totalEjemplares;

    @NotNull
    private Integer disponibles;

    // Convierte DTO a entidad
    public Libro toEntity() {
        return Libro.builder()
                .titulo(this.titulo)
                .autor(this.autor)
                .isbn(this.isbn)
                .totalEjemplares(this.totalEjemplares)
                .disponibles(this.disponibles)
                .build();
    }
}
