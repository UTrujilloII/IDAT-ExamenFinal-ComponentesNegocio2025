package com.biblioteca.api.controller;

import com.biblioteca.api.dto.LibroRequestDTO;
//import io.swagger.v3.oas.annotations.Parameter;
import com.biblioteca.api.dto.LibroResponseDTO;
import com.biblioteca.api.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "Operaciones CRUD de libros")
@CrossOrigin(origins = "*") // Permite acceso desde Postman, navegador, etc.
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // =========================================
    // 1️⃣ LISTAR TODOS LOS LIBROS (GET)
    // =========================================
    @GetMapping
    @Operation(summary = "Listar libros")
    public ResponseEntity<List<LibroResponseDTO>> listarLibros() {
        return ResponseEntity.ok(libroService.listar());
    }

    // =========================================
    // 2️⃣ OBTENER LIBRO POR ID (GET /id)
    // =========================================
    //@GetMapping("/{id}")
    //public String testSwagger(@PathVariable Long id) {
    //    return "OK " + id;
    //}

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener libro por ID",
            description = "Devuelve un libro según su identificador único"
    )
    public ResponseEntity<LibroResponseDTO> obtenerPorId(
            @PathVariable Long id
    ) {
        LibroResponseDTO libro = libroService.obtenerPorId(id);
        return ResponseEntity.ok(libro);
    }

    // 2️⃣ REGISTRAR NUEVO LIBRO
    @PostMapping
    @Operation(summary = "Registrar nuevo libro")
    public ResponseEntity<LibroResponseDTO> guardar(@RequestBody LibroRequestDTO dto) {
        return ResponseEntity.ok(libroService.guardar(dto));
    }

    // =========================================
    // 3️⃣ ACTUALIZAR LIBRO (PUT /id)
    // =========================================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro por ID")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(
            @PathVariable Long id,
            @RequestBody LibroRequestDTO dto) {

        // Llamamos al servicio que actualiza el libro
        LibroResponseDTO actualizado = libroService.actualizar(id, dto);

        return ResponseEntity.ok(actualizado);
    }

    // =========================================
    // 3️⃣ ELIMINAR LIBRO (DELETE /id)
    // =========================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {

        libroService.eliminar(id);

        return ResponseEntity.ok("Libro eliminado correctamente.");
    }
}

