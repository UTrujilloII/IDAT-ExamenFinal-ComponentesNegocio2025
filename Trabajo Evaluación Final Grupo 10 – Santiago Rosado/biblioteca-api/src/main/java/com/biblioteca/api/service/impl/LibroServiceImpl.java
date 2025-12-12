package com.biblioteca.api.service.impl;

import com.biblioteca.api.dto.LibroRequestDTO;
import com.biblioteca.api.dto.LibroResponseDTO;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.mapper.LibroMapper;
import com.biblioteca.api.model.Libro;
import com.biblioteca.api.repository.LibroRepository;
import com.biblioteca.api.service.LibroService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * =========================================================
 * IMPLEMENTACIÓN DEL SERVICIO DE LIBROS
 * Contiene la lógica del negocio para gestionar libros.
 * =========================================================
 */
@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    // Constructor requerido por Spring (inyección de dependencias)
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<LibroResponseDTO> listar() {
        return libroRepository.findAll().stream()
                .map(LibroMapper::toDTO)
                .toList();
    }

    @Override
    public LibroResponseDTO guardar(LibroRequestDTO dto) {
        Libro libro = LibroMapper.toEntity(dto);
        Libro guardado = libroRepository.save(libro);
        return LibroMapper.toDTO(guardado);
    }

    /**
     * ==========================================
     *  Obtener un libro por su ID
     *  Si no existe, lanza excepción personalizada
     * ==========================================
     */
    @Override
    public LibroResponseDTO obtenerPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        return LibroMapper.toDTO(libro);
    }

    /**
     * ==========================================
     *  Actualizar un libro por su ID
     *  Si no existe, lanza excepción personalizada
     * ==========================================
     */
    @Override
    public LibroResponseDTO actualizar(Long id, LibroRequestDTO dto) {

        // Buscamos el libro
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        // Actualizamos valores
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setAnioPublicacion(dto.getAnioPublicacion());

        // Guardamos cambios
        Libro actualizado = libroRepository.save(libro);

        // Retornamos DTO actualizado
        return LibroMapper.toDTO(actualizado);
    }

    /**
     * ==========================================
     *  Eliminar un libro por su ID
     *  Si no existe, lanza excepción personalizada
     * ==========================================
     */
    @Override
    public void eliminar(Long id) {

        // Buscar el libro antes de eliminar
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        // Eliminar el libro
        libroRepository.delete(libro);
    }

}
