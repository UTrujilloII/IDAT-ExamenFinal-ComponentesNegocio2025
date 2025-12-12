package com.biblioteca.biblioteca_api.controllers;

import com.biblioteca.biblioteca_api.dtos.libro.LibroDTO;
import com.biblioteca.biblioteca_api.dtos.libro.LibroResponseDTO;
import com.biblioteca.biblioteca_api.entity.Libro;
import com.biblioteca.biblioteca_api.services.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> listar() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<LibroResponseDTO> libros = (esAdmin
                ? libroService.listarTodosAdmin()        // admin → todos
                : libroService.listarTodos())            // user → solo activos
                .stream()
                .map(LibroResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(LibroResponseDTO.fromEntity(libroService.obtenerPorId(id)));
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> crear(@RequestBody @Valid LibroDTO libroDTO) {
        Libro creado = libroService.crearLibro(libroDTO.toEntity());
        return ResponseEntity.ok(LibroResponseDTO.fromEntity(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizar(@PathVariable Long id,
                                                       @RequestBody @Valid LibroDTO libroDTO) {
        Libro actualizado = libroService.actualizarLibro(id, libroDTO.toEntity());
        return ResponseEntity.ok(LibroResponseDTO.fromEntity(actualizado));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> eliminar(@PathVariable Long id) {
        Libro eliminado = libroService.eliminarLibro(id);
        return ResponseEntity.ok(LibroResponseDTO.fromEntity(eliminado));
    }

    @PutMapping("/activar/{id}")                    // esto agregue
    public ResponseEntity<LibroResponseDTO> activar(@PathVariable Long id) {
        Libro activado = libroService.activarLibro(id);
        return ResponseEntity.ok(LibroResponseDTO.fromEntity(activado));
    }

}
