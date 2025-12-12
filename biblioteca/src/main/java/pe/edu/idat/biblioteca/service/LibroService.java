package pe.edu.idat.biblioteca.service;

import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

import java.util.List;

public interface LibroService {
    LibroResponse createLibro (LibroRequest libroRequest);
    LibroResponse findById(Long id);
    List<LibroResponse>listarLibros();


    LibroResponse updateLibro(Long id,LibroRequest libroRequest);
    void deleteLibro(Long id);





    void reducirStock(Libro libro);
    void aumentarStock(Libro libro);
}
