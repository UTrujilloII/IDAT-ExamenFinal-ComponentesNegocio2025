package com.proyecto.idat.ms_gestion_ventas.dto.prestamo;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


//   DTO que usa el ADMIN para actualizar CUALQUIER préstamo.
  // Puede cambiar:
  // - fecha de devolución
  // - libro asociado
  // - usuario al que pertenece el préstamo

public record ActualizarPrestamoAdminRequest(

        @NotNull(message = "La nueva fecha de devolución es obligatoria")
        @FutureOrPresent(message = "La fecha de devolución no puede ser anterior a hoy")
        LocalDate fechaDevolucion,

        @NotNull(message = "El id del libro es obligatorio")
        @Min(value = 1, message = "El id del libro debe ser mayor o igual a 1")
        Long libroId,

        @NotNull(message = "El id del usuario es obligatorio")
        @Min(value = 1, message = "El id del usuario debe ser mayor o igual a 1")
        Long usuarioId
) {
}
