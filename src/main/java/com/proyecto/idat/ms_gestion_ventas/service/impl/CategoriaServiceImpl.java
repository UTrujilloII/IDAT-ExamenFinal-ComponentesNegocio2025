package com.proyecto.idat.ms_gestion_ventas.service.impl;

import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Categoria;
import com.proyecto.idat.ms_gestion_ventas.exception.RecursoNoEncontradoException;
import com.proyecto.idat.ms_gestion_ventas.mappers.CategoriaMapper;
import com.proyecto.idat.ms_gestion_ventas.repository.CategoryRepository;
import com.proyecto.idat.ms_gestion_ventas.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    // Usamos SIEMPRE el mismo nombre de variable para evitar confusiones
    private final CategoryRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = categoriaMapper.toEntity(request);
        Categoria guardada = categoriaRepository.save(categoria);
        return categoriaMapper.toResponse(guardada);
    }

    @Override
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    @Override
    public CategoriaResponse obtenerPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la categoría con id " + idCategoria
                ));

        return categoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse actualizar(Long idCategoria, CategoriaRequest request) {

        // Buscamos la categoría existente
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con id " + idCategoria
                ));

        // Solo actualizamos campos editables (el id no se toca)
        categoria.setNombre(request.nombre());

        Categoria guardada = categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(guardada);
    }
}
