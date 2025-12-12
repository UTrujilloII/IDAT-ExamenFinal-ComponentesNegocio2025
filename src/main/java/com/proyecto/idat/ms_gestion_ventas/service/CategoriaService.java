package com.proyecto.idat.ms_gestion_ventas.service;

import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    CategoriaResponse actualizar(Long idCategoria, CategoriaRequest request);

    CategoriaResponse crear(CategoriaRequest request);

    List<CategoriaResponse> listar();

    CategoriaResponse obtenerPorId(Long idCategoria);
}
