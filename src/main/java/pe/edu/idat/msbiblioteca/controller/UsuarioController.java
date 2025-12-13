package pe.edu.idat.msbiblioteca.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.msbiblioteca.dto.auth.RoleUpdateRequest;
import pe.edu.idat.msbiblioteca.dto.user.UsuarioRequestDTO;
import pe.edu.idat.msbiblioteca.dto.user.UsuarioResponseDTO;
import pe.edu.idat.msbiblioteca.entity.Rol;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.exception.ResourceNotFoundException;
import pe.edu.idat.msbiblioteca.repository.RolRepository;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        log.info("PUT /v1/usuarios/{}/roles - cambiar rol", id);
        try {
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
        } catch (Exception e) {
            log.error("Error en updateUserRole", e);
            throw e;
        }
    }

    // Nuevo CRUD para usuarios

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        log.info("GET /v1/usuarios - listarUsuarios");
        try {
            List<UsuarioResponseDTO> list = usuarioRepository.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error en listarUsuarios", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /v1/usuarios/{} - obtenerUsuario", id);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String requester = auth.getName();

            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

            // Si no es ADMIN, permitir solo si es el propio usuario
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin && !usuario.getUsername().equals(requester)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (Exception e) {
            log.error("Error en obtenerUsuario", e);
            throw e;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("POST /v1/usuarios - crearUsuario username={}", dto.getUsername());
        try {
            // Validaciones básicas
            if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(dto.getUsername());
            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario.setNombreCompleto(dto.getNombreCompleto());
            usuario.setEmail(dto.getEmail());
            usuario.setTelefono(dto.getTelefono());
            usuario.setDireccion(dto.getDireccion());
            usuario.setEnabled(true);

            // Asignar rol
            String roleName = dto.getRole() == null || dto.getRole().isBlank() ? "ROLE_USER" : dto.getRole().toUpperCase();
            if (!roleName.startsWith("ROLE_")) roleName = "ROLE_" + roleName;
            final String finalRoleName2 = roleName; // hacer efectivamente final para la lambda
            Rol rol = rolRepository.findByNombre(finalRoleName2).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + finalRoleName2));
            Set<Rol> roles = new HashSet<>(); roles.add(rol);
            usuario.setRoles(roles);

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(usuario));
        } catch (Exception e) {
            log.error("Error en crearUsuario", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("PUT /v1/usuarios/{} - actualizarUsuario", id);
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

            // Actualizar campos permitidos
            usuario.setNombreCompleto(dto.getNombreCompleto());
            usuario.setEmail(dto.getEmail());
            usuario.setTelefono(dto.getTelefono());
            usuario.setDireccion(dto.getDireccion());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(toResponseDTO(usuario));
        } catch (Exception e) {
            log.error("Error en actualizarUsuario", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("DELETE /v1/usuarios/{} - eliminarUsuario", id);
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

            usuarioRepository.delete(usuario);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error en eliminarUsuario", e);
            throw e;
        }
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        UsuarioResponseDTO r = new UsuarioResponseDTO();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setNombreCompleto(u.getNombreCompleto());
        r.setEmail(u.getEmail());
        r.setTelefono(u.getTelefono());
        r.setDireccion(u.getDireccion());
        r.setEnabled(u.isEnabled());
        r.setFechaRegistro(u.getFechaRegistro());
        r.setRoles(u.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet()));
        return r;
    }
}
