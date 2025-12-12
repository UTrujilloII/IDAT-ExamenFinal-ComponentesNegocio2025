package com.biblioteca.biblioteca_api.dtos.prestamo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrestamoRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long libroId;

    @NotNull
    private LocalDate fechaPrestamo;

    @NotNull
    private LocalDate fechaDevolucionEstimada;

    @NotNull
    private LocalDate fechaDevolucionReal;

    @NotNull
    private String estado;
}
