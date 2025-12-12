package com.proyecto.idat.ms_gestion_ventas.controller;

import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroResponse;
import com.proyecto.idat.ms_gestion_ventas.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    // crear libros (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LibroResponse> crear(@Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.crear(request));
    }

    // Listar libros (ADMIN - USUARIO)
    @GetMapping
    public ResponseEntity<List<LibroResponse>> listar() {
        return ResponseEntity.ok(libroService.listar());
    }

    // obtener libro por id (USUARIO;ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    //  actualizar libro (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest request
    ) {
        return ResponseEntity.ok(libroService.actualizar(id, request));
    }

    // eliminar libro (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Libro eliminado con éxito");
        body.put("idLibro", id);

        return ResponseEntity.ok(body);
    }

}
