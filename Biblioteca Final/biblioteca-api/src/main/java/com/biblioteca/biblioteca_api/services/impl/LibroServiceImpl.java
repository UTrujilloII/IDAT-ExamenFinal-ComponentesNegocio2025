package com.biblioteca.biblioteca_api.services.impl;

import com.biblioteca.biblioteca_api.entity.Libro;
import com.biblioteca.biblioteca_api.repository.LibroRepository;
import com.biblioteca.biblioteca_api.services.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    @Override
    public Libro crearLibro(Libro libro) {
        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con este ISBN");
        }
        return libroRepository.save(libro);
    }

    @Override
    public Libro obtenerPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Override                                    //esto cambie ctrl + Z pa volver
    public List<Libro> listarTodos() {
        return libroRepository.findByActivoTrue();
    }

    @Override
    public List<Libro> listarTodosAdmin() {
        return libroRepository.findAll(); // devuelve activos e inactivos
    }


    @Override
    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Libro existente = obtenerPorId(id);

        existente.setTitulo(libroActualizado.getTitulo());
        existente.setAutor(libroActualizado.getAutor());
        existente.setIsbn(libroActualizado.getIsbn());
        existente.setTotalEjemplares(libroActualizado.getTotalEjemplares());
        existente.setDisponibles(libroActualizado.getDisponibles());

        return libroRepository.save(existente);
    }

    @Transactional                              // esto cambie ctrl + Z pa volver
    @Override
    public Libro eliminarLibro(Long id) {
        Libro libro = obtenerPorId(id);
        libro.setActivo(false);
        libroRepository.save(libro);
        return libro;
    }

    @Transactional                             // esto agregue
    public Libro activarLibro(Long id) {
        Libro libro = obtenerPorId(id);
        libro.setActivo(true);
        libroRepository.save(libro);
        return libro;
    }



}
