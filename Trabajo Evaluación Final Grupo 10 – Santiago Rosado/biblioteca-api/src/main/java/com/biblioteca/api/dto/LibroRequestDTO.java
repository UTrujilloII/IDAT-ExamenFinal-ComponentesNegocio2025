package com.biblioteca.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para recibir datos del usuario cuando crea o edita un libro.
 * Aquí aplicamos validaciones para garantizar datos correctos.
 */
@Getter
@Setter
@Schema(description = "Datos para crear o actualizar un libro")
public class LibroRequestDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @NotNull(message = "El año es obligatorio")
    private Integer anioPublicacion;

    @NotNull(message = "La cantidad disponible es obligatoria")
    private Integer cantidadDisponible;
}