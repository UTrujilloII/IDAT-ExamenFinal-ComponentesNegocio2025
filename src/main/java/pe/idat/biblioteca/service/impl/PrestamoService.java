package pe.idat.biblioteca.service.impl;

import pe.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.idat.biblioteca.dto.prestamo.PrestamoResponse;

import java.util.List;

public interface PrestamoService {
    PrestamoResponse registarPrestamo(PrestamoRequest prestamoRequest);
    void registarDevolucion(Long idprestamo);
    List<PrestamoResponse> obtenerMisPrestamosPorUsername(String username);
    List<PrestamoResponse> obtenerTodosPrestamos();


}
