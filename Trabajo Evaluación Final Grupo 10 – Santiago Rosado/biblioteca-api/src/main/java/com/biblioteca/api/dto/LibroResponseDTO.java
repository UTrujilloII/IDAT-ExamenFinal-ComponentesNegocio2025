package com.biblioteca.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para devolver datos al usuario de forma controlada.
 * No mostramos información interna de la base de datos.
 */
@Getter
@Setter
public class LibroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private Integer anioPublicacion;
}
