package com.proyecto.idat.ms_gestion_ventas.service;

import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroResponse;

import java.util.List;

public interface LibroService {

    LibroResponse crear(LibroRequest request);

    List<LibroResponse> listar();

    // obtener un libro por id con mensaje claro si no existe
    LibroResponse obtenerPorId(Long idLibro);

    // eliminar libro (solo ADMIN)
    void eliminar(Long idLibro);

    // actualizar libro (solo ADMIN)
    LibroResponse actualizar(Long idLibro, LibroRequest request);
}
