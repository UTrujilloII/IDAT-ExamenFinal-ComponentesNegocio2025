package com.proyecto.idat.ms_gestion_ventas.dto.prestamo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;



import java.time.LocalDate;

public record PrestamoRequest(

        @NotNull(message = "El id del libro es obligatorio")
        @Min(value = 1, message = "El id del libro debe ser positivo")
        Long idLibro,

        @NotNull(message = "El id del usuario es obligatorio")
        @Min(value = 1, message = "El id del usuario debe ser positivo")
        Long idUsuario,


        @NotNull(message = "La fecha de devolución es obligatoria")
        @Future(message = "La fecha de devolución debe ser al menos un día después de hoy")
        LocalDate fechaDevolucion

) {
}
