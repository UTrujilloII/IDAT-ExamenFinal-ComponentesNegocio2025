package com.biblioteca.biblioteca_api.services;

import com.biblioteca.biblioteca_api.entity.Prestamo;
import java.util.List;

public interface PrestamoService {

    Prestamo crearPrestamo(Long usuarioId, Long libroId);

    Prestamo devolverPrestamo(Long prestamoId);

    List<Prestamo> obtenerPorUsuario(Long usuarioId);

    // 🔹 Agregar este método
    List<Prestamo> listarTodos();

    Prestamo obtenerPorId(Long id);
}
