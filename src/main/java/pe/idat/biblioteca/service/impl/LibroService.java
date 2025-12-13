package pe.idat.biblioteca.service.impl;

import pe.idat.biblioteca.dto.libro.LibroRequest;
import pe.idat.biblioteca.dto.libro.LibroResponse;

import java.util.List;

public interface LibroService {
    LibroResponse crearLibro(LibroRequest libroRequest);
    LibroResponse obtenerLibroPorid(Long id);
    LibroResponse actualizarLibro(Long id, LibroRequest libroRequest);
    List<LibroResponse> listarTodosLosLibros();
    public void desactivarLibro(Long id);
    public void reactivarLibro(Long id);
}
