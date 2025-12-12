package pe.edu.idat.biblioteca.dto.prestamo;

import jakarta.validation.Valid; // Necesario para validar la lista de ítems
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty; // Nuevo import para asegurar que la lista no esté vacía
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List; // Nuevo import para usar List

public record PrestamoRequest(

        @NotNull(message = "El ID del usuario es requerido")
        @Min(value = 1, message = "El ID del usuario debe ser válido")
        Long usuarioId,

        // ------------------------------------------------------------------
        // --- CAMBIO CLAVE: Reemplazamos Long libroId y Integer cantidad ---
        // ------------------------------------------------------------------
        @Valid // Asegura que cada PrestamoItemRequest dentro de la lista sea validado
        @NotEmpty(message = "Debe solicitar al menos un libro en el pedido.")
        @NotNull(message = "La lista de ítems no puede ser nula.")
        List<PrestamoItemRequest> items, // AHORA RECIBE UNA LISTA DE LIBROS
        // ------------------------------------------------------------------

        // --- CAMPO OPCIONAL (si el Admin lo quiere definir) ---
        LocalDate fechaPrestamo,

        // --- Campo obligatorio para la devolución ---
        @NotNull(message = "La fecha de devolución estimada es requerida")
        LocalDate fechaDevolucionEstimada
) {}