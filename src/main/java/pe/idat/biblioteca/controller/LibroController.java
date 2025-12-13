package pe.idat.biblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.biblioteca.dto.libro.LibroRequest;
import pe.idat.biblioteca.dto.libro.LibroResponse;
import pe.idat.biblioteca.service.impl.LibroService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService libroService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest libroRequest){
        LibroResponse libroResponse = libroService.crearLibro(libroRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(libroResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest libroRequest) {
        LibroResponse libro = libroService.actualizarLibro(id, libroRequest);
        return ResponseEntity.ok(libro);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarlibroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerLibroPorid(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<LibroResponse>> listarLibros() {
        List<LibroResponse> libros = libroService.listarTodosLosLibros();
        return ResponseEntity.ok(libros);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivarLibro(@PathVariable Long id) {
        libroService.desactivarLibro(id);
        return ResponseEntity.ok(Map.of("message", "Libro desactivado correctamente"));
    }


    @PatchMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reactivarLibro(@PathVariable Long id) {
        libroService.reactivarLibro(id);
        return ResponseEntity.ok(Map.of("message", "Libro reactivado correctamente"));
    }
}
