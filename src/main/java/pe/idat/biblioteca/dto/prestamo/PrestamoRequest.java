package pe.idat.biblioteca.dto.prestamo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PrestamoRequest(
        @NotNull(message = "La fecha de préstamo es obligatoria")
        @PastOrPresent(message = "La fecha de préstamo no puede ser futura")
              LocalDate fechaPrestamo,

          @NotNull(message = "La fecha de devolución es obligatoria")
          @Future(message = "La fecha de devolución debe ser futura")
          LocalDate fechaDevolucion,

          @NotNull(message = "El ID del usuario es obligatorio")
          Long idUsuario,

          @NotNull(message = "El ID del libro es obligatorio")
          Long idLibro) {
    }
