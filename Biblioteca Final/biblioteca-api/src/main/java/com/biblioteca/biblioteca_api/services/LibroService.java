package com.biblioteca.biblioteca_api.services;

import com.biblioteca.biblioteca_api.dtos.libro.LibroDTO;
import com.biblioteca.biblioteca_api.entity.Libro;
import java.util.List;

public interface LibroService {

    Libro crearLibro(Libro libro);

    Libro obtenerPorId(Long id);

    List<Libro> listarTodos();

    Libro actualizarLibro(Long id, Libro libro);

    Libro eliminarLibro(Long id);

    Libro activarLibro(Long id);

    List<Libro> listarTodosAdmin();

}
