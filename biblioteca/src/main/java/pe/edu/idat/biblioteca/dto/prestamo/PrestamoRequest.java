package pe.edu.idat.biblioteca.dto.prestamo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record PrestamoRequest(

        @NotNull(message = "El ID del usuario es requerido")
        @Min(value = 1, message = "El ID del usuario debe ser válido")
        Long usuarioId,
        @Valid
        @NotEmpty(message = "Debe solicitar al menos un libro en el pedido.")
        @NotNull(message = "La lista de ítems no puede ser nula.")
        List<PrestamoItemRequest> items,
        LocalDate fechaPrestamo,
        @NotNull(message = "La fecha de devolución estimada es requerida")
        LocalDate fechaDevolucionEstimada
) {}