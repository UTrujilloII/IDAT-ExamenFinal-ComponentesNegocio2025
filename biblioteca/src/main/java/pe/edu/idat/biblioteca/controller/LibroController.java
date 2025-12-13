package pe.edu.idat.biblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.dto.libro.LibroPatchRequest;
import pe.edu.idat.biblioteca.service.impl.LibroService;

import java.util.List;

@RestController
@RequestMapping("/v1/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    // --- 1. CREAR LIBRO (POST) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LibroResponse> createLibro(@Valid @RequestBody LibroRequest libroRequest) {
        LibroResponse libroResponse = libroService.registrarLibro(libroRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(libroResponse);
    }

    // --- 2. LISTAR TODOS (GET) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<LibroResponse>> listarTodos() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    // --- 3. OBTENER POR ID (GET) ---
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> getLibro(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerLibro(id));
    }

    // --- 4A. ACTUALIZAR LIBRO COMPLETO (PUT) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> updateLibro(@PathVariable Long id,
                                                     @Valid @RequestBody LibroRequest libroRequest) {
        LibroResponse libroResponse = libroService.actualizarLibro(id, libroRequest);
        return ResponseEntity.ok(libroResponse);
    }

    // --- 4B. ACTUALIZAR LIBRO PARCIAL (PATCH) ---
    // Solo se enviarán los campos a modificar (ej. solo el stock)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarParcialLibro(@PathVariable Long id,
                                                                @Valid @RequestBody LibroPatchRequest request) {
        LibroResponse updated = libroService.actualizarParcialLibro(id, request);
        return ResponseEntity.ok(updated);
    }

    // --- 5. ELIMINAR LIBRO (DELETE) ---
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}