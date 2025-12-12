package pe.edu.idat.biblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // ¡NUEVO IMPORT!
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioPatchRequest; // ¡NUEVO IMPORT DEL DTO DE PATCH!
// Importar la INTERFAZ (asumo que es pe.edu.idat.biblioteca.service.UsuarioService)
import pe.edu.idat.biblioteca.service.impl.UsuarioService;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // --- 1. CREAR USUARIO (POST) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponse> createUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioResponse = usuarioService.crearUsuario(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
    }

    // --- 2. LISTAR USUARIOS (GET) ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> listaUsuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(listaUsuarios);
    }

    // --- 3A. ACTUALIZAR USUARIO COMPLETO (PUT) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id,
                                                             @Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse updated = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(updated);
    }

    // --- 3B. ACTUALIZAR USUARIO PARCIAL (PATCH) ---
    // Recibe el DTO con campos opcionales
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarParcialUsuario(@PathVariable Long id,
                                                                    @Valid @RequestBody UsuarioPatchRequest request) {
        UsuarioResponse updated = usuarioService.actualizarParcialUsuario(id, request);
        return ResponseEntity.ok(updated);
    }


    // --- 4. ELIMINAR USUARIO (DELETE) ---
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}