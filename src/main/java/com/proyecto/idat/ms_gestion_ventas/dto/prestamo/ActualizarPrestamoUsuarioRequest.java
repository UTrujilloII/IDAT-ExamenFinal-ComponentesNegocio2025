package com.proyecto.idat.ms_gestion_ventas.dto.prestamo;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


//  DTO que usa un USUARIO para actualizar uno de sus propios préstamos.
//  Solo puede cambiar:
//   - la fecha de devolución
//   - el libro asociado al préstamo

public record ActualizarPrestamoUsuarioRequest(

        @NotNull(message = "La nueva fecha de devolución es obligatoria")
        @FutureOrPresent(message = "La fecha de devolución no puede ser anterior a hoy")
        LocalDate fechaDevolucion,

        @NotNull(message = "El id del libro es obligatorio")
        @Min(value = 1, message = "El id del libro debe ser mayor o igual a 1")
        Long libroId
) {
}
