package pe.edu.idat.biblioteca.dto.prestamo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PrestamoRequest(
        @NotNull(message = "El ID del usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El ID del libro es obligatorio")
        Long libroId,

        @NotNull(message = "La fecha de devolución esperada es obligatoria")
        @Future(message = "La fecha de devolución no puede ser igual a la fecha de prestamo")
        LocalDate fechaDevolucionEsperada
) {}