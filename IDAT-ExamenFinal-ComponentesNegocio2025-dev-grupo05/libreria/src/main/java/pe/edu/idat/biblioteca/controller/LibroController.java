package pe.edu.idat.biblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.service.impl.LibroService;


import java.util.List;

@RestController
@RequestMapping("/v1/libros") // Usaremos '/v1' como prefijo estándar
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @GetMapping // /v1/libros (Lista todos, incluyendo inactivos si la lógica lo permite)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Acceso general
    public ResponseEntity<List<LibroResponse>> listarLibros() {
        // Renombrado para coincidir con el servicio
        return ResponseEntity.ok(libroService.listarTodos());
    }

    @GetMapping("/{id}") // /v1/libros/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<LibroResponse> obtenerLibro(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerLibro(id));
    }

    @PostMapping // /v1/libros
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest request) {
        // Renombrado para coincidir con el servicio
        return ResponseEntity.ok(libroService.registrarLibro(request));
    }

    @PutMapping("/{id}") // /v1/libros/{id}
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibroResponse> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.actualizarLibro(id, request));
    }

    @DeleteMapping("/{id}") // /v1/libros/{id} (Eliminación Lógica/Inactivación)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inactivarLibro(@PathVariable Long id) {

        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}