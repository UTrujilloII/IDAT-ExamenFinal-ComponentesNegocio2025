package pe.edu.idat.biblioteca_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca_api.model.Libro;
import pe.edu.idat.biblioteca_api.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Gestión de Libros", description = "Control de inventario (Requiere permisos de ADMIN)")
public class LibroController {

    private final LibroService libroService;

    // 1. Obtener todos los libros (Cualquier usuario autenticado puede verlos)
    @Operation(summary = "Listar Libros", description = "Obtiene el catálogo completo de libros disponibles.")
    @GetMapping
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    // 2. Crear un libro (Solo ADMIN - Validado en SecurityConfig)
    @Operation(
        summary = "Crear Libro", 
        description = "Registra un nuevo libro en la base de datos (Solo ADMIN).",
        security = @SecurityRequirement(name = "bearerAuth") // <--- Indica que requiere el candadito
    )
    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.guardar(libro));
    }
}