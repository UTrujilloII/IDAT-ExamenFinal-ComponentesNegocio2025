package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.dto.libro.LibroPatchRequest; // ¡IMPORTANTE: Importar el DTO parcial!

import java.util.List;

public interface LibroService {
    LibroResponse registrarLibro(LibroRequest request);
    List<LibroResponse> listarTodos();
    LibroResponse obtenerLibro(Long id);
    LibroResponse actualizarLibro(Long id, LibroRequest request);

    // --- NUEVA FIRMA PARA EL MÉTODO PATCH ---
    LibroResponse actualizarParcialLibro(Long id, LibroPatchRequest request);
    // ----------------------------------------

    void eliminarLibro(Long id);
    List<LibroResponse> listarDisponibles();
}