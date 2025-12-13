package pe.edu.idat.msbiblioteca.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.msbiblioteca.dto.auth.RoleUpdateRequest;
import pe.edu.idat.msbiblioteca.entity.Rol;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.exception.ResourceNotFoundException;
import pe.edu.idat.msbiblioteca.repository.RolRepository;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Normalize role: accept "ADMIN" or "ROLE_ADMIN"
        String roleName = request.role() == null || request.role().isBlank() ? "ROLE_USER" : request.role().toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        final String finalRoleName = roleName; // hacer efectiva final para usar en lambda

        Rol rol = rolRepository.findByNombre(finalRoleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + finalRoleName));

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        usuario.setRoles(roles);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
    }
}

