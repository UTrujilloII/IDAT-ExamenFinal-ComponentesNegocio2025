package com.proyecto.idat.ms_gestion_ventas.dto.libro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record LibroRequest(

        @NotBlank(message = "El título es obligatorio")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        String autor,

        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(
                regexp = "^(\\d{9}[\\dX]|\\d{13})$",
                message = "El ISBN debe tener 10 o 13 caracteres válidos: solo dígitos y opcionalmente una 'X' al final para ISBN-10"
        )
        String isbn,

        @PastOrPresent(message = "La fecha de publicación no puede ser futura")
        LocalDate fechaPublicacion,

        @NotNull(message = "La cantidad de ejemplares totales es obligatoria")
        @Min(value = 1, message = "Debe haber al menos 1 ejemplar")
        Integer ejemplaresTotales,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {
}
