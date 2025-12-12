package com.proyecto.idat.ms_gestion_ventas.controller;

import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaResponse;
import com.proyecto.idat.ms_gestion_ventas.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // ADMIN crea categorías
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.crear(request));
    }


    // Listar todas las categorías (ADMIN - USUARIO)
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }



    // Obtener categoría por id (ADMIN - USUARIO)
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }


    // actualizar categoría por id (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idCategoria}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Long idCategoria,
            @Valid @RequestBody CategoriaRequest request
    ) {
        CategoriaResponse response = categoriaService.actualizar(idCategoria, request);
        return ResponseEntity.ok(response);
    }


}
