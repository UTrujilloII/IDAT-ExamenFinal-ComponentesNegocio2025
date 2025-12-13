package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.dto.libro.LibroPatchRequest;

import java.util.List;

public interface LibroService {
    LibroResponse registrarLibro(LibroRequest request);
    List<LibroResponse> listarTodos();
    LibroResponse obtenerLibro(Long id);
    LibroResponse actualizarLibro(Long id, LibroRequest request);
    LibroResponse actualizarParcialLibro(Long id, LibroPatchRequest request);


    void eliminarLibro(Long id);
    List<LibroResponse> listarDisponibles();
}