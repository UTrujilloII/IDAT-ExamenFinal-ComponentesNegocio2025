package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;

import java.util.List;
public interface PrestamoService {
    List<PrestamoResponse> crearPrestamo(PrestamoRequest request);
    void devolverPrestamo(Long id);
    List<PrestamoResponse> obtenerHistorial(String username);
    PrestamoResponse obtenerPrestamoPorId(Long id);
    List<PrestamoResponse> listarTodosLosPrestamos();
}