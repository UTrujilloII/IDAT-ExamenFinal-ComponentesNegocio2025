package pe.edu.idat.biblioteca_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca_api.model.Usuario;
import pe.edu.idat.biblioteca_api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    // 1. ACTUALIZAR USUARIO (Sin tocar contraseña)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos solo datos informativos
        usuario.setNombres(usuarioDetalles.getNombres());
        usuario.setApellidos(usuarioDetalles.getApellidos());
        usuario.setEmail(usuarioDetalles.getEmail());
        
        // Opcional: Actualizar username si lo envían
        if (usuarioDetalles.getUsername() != null && !usuarioDetalles.getUsername().isEmpty()) {
            usuario.setUsername(usuarioDetalles.getUsername());
        }

        // NO tocamos el password. Se mantiene el que ya estaba en base de datos.
        
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario actualizado correctamente (Contraseña sin cambios)");
    }

    // 2. ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("El usuario con ID " + id + " no existe.");
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}