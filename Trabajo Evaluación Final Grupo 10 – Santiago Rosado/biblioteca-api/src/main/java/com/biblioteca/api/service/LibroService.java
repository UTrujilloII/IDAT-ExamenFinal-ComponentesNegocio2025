package com.biblioteca.api.service;

import com.biblioteca.api.dto.LibroRequestDTO;
import com.biblioteca.api.dto.LibroResponseDTO;

import java.util.List;

/**
 * =========================================================
 *  SERVICIO DE GESTIÓN DE LIBROS
 *  Interfaz que define el contrato de operaciones para la
 *  capa de servicio. Aquí solo se describen los métodos,
 *  la lógica se implementa en LibroServiceImpl.java.
 * =========================================================
 */
public interface LibroService {

    // Listar todos los libros
    List<LibroResponseDTO> listar();

    // Registrar un nuevo libro
    LibroResponseDTO guardar(LibroRequestDTO dto);

    // Obtener un libro específico por su ID
    LibroResponseDTO obtenerPorId(Long id);

    // Actualiza un libro específico por su ID
    LibroResponseDTO actualizar(Long id, LibroRequestDTO dto);

    // Eliminar un libro específico por su ID
    void eliminar(Long id);

}
