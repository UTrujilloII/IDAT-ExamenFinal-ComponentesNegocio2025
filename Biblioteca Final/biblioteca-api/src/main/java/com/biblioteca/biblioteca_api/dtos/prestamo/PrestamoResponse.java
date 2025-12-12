package com.biblioteca.biblioteca_api.dtos.prestamo;

import com.biblioteca.biblioteca_api.entity.Prestamo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponse {

    private Long id;
    private String usuario;
    private String libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private String estado;

    public static PrestamoResponse fromEntity(Prestamo prestamo) {
        return PrestamoResponse.builder()
                .id(prestamo.getId())
                .usuario(prestamo.getUsuario().getNombre())
                .libro(prestamo.getLibro().getTitulo())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEstimada(prestamo.getFechaDevolucionEstimada())
                .fechaDevolucionReal(prestamo.getFechaDevolucionReal())
                .estado(prestamo.getEstado().name())
                .build();
    }
}
