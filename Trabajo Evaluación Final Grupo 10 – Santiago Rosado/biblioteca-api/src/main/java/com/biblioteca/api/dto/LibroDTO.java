package com.biblioteca.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO LIBRO
 */
@Getter
@Setter
public class LibroDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1000, message = "Año inválido")
    private Integer anioPublicacion;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 1, message = "Debe haber al menos un ejemplar")
    private Integer cantidadDisponible;
}
