package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;

import java.util.List;

public interface LibroService {


    LibroResponse registrarLibro(LibroRequest request);
    LibroResponse actualizarLibro(Long id, LibroRequest request);
    void eliminarLibro(Long id);

    // Crud
    LibroResponse obtenerLibro(Long id);
    List<LibroResponse> listarTodos();
    List<LibroResponse> listarDisponibles();


    LibroResponse actualizarParcialLibro(Long id, Object request);
}