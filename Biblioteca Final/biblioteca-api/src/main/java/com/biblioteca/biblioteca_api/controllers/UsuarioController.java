package com.biblioteca.biblioteca_api.controllers;

import com.biblioteca.biblioteca_api.dtos.usuario.UsuarioResponseDTO;
import com.biblioteca.biblioteca_api.entity.Usuario;
import com.biblioteca.biblioteca_api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    // ✅ Listar todos los usuarios (solo para admin)
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarios);
    }
}

