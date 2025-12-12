package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;

import java.util.List;

public interface PrestamoService {
    PrestamoResponse crearPrestamo(PrestamoRequest request);
    PrestamoResponse registrarDevolucion(Long prestamoId);

    // Métodos de búsqueda/historial
    List<PrestamoResponse> listarHistorialUsuario(Long usuarioId);
    List<PrestamoResponse> listarPrestamosActivos();
}