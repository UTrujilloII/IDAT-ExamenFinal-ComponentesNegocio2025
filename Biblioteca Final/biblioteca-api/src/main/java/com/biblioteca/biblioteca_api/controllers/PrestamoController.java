package com.biblioteca.biblioteca_api.controllers;

import com.biblioteca.biblioteca_api.dtos.prestamo.PrestamoRequest;
import com.biblioteca.biblioteca_api.dtos.prestamo.PrestamoResponse;
import com.biblioteca.biblioteca_api.entity.Prestamo;
import com.biblioteca.biblioteca_api.entity.Usuario;
import com.biblioteca.biblioteca_api.services.PrestamoService;
import com.biblioteca.biblioteca_api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;

    // 🟦 ADMIN: Registrar préstamo
    @PostMapping("/admin")
    public ResponseEntity<PrestamoResponse> crear(@RequestBody PrestamoRequest request) {
        Prestamo prestamo = prestamoService.crearPrestamo(request.getUsuarioId(), request.getLibroId());
        return ResponseEntity.ok(PrestamoResponse.fromEntity(prestamo));
    }

    // 🟦 ADMIN: Devolver préstamo
    @PutMapping("/admin/{id}/devolver")
    public ResponseEntity<PrestamoResponse> devolver(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.devolverPrestamo(id);
        return ResponseEntity.ok(PrestamoResponse.fromEntity(prestamo));
    }

    // 🟦 USUARIO: Ver sus propios préstamos
    @GetMapping("/mios")
    public ResponseEntity<List<PrestamoResponse>> misPrestamos() {
        // Obtener usuario autenticado desde JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        List<PrestamoResponse> prestamos = prestamoService.obtenerPorUsuario(usuario.getId())
                .stream()
                .map(PrestamoResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(prestamos);
    }

    // 🟦 ADMIN: Ver todos los préstamos
    @GetMapping("/admin")
    public ResponseEntity<List<PrestamoResponse>> todos() {
        List<PrestamoResponse> prestamos = prestamoService.listarTodos()
                .stream()
                .map(PrestamoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestamos);
    }
}
