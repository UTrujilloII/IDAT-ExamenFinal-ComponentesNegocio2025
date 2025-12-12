package pe.edu.idat.biblioteca.dto.prestamo;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrestamoRequest (

        @NotNull(message = "el campo fecha entrega es obligatorio")
        LocalDate fechaEntrega,

        @NotNull(message = "el campo idLibro es obligatorio")
        Long idLibro,

        @NotNull(message = "el campo idUsuario es obligatorio")
        Long idUsuario
){}
