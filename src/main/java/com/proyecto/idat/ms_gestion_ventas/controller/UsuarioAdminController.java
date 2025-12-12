package com.proyecto.idat.ms_gestion_ventas.controller;

import com.proyecto.idat.ms_gestion_ventas.dto.usuario.AdminCrearUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.usuario.ActualizarMiCuentaRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.usuario.ActualizarUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.usuario.UsuarioResponse;
import com.proyecto.idat.ms_gestion_ventas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// Controlador de administración de usuarios.

//  IMPORTANTE:
//   - Aquí NO se hace lógica de negocio.
//  - Solo delega en UsuarioService, que es quien trabaja con repositorios, encoder, etc.

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    // ÚNICA dependencia: el servicio de usuarios
    private final UsuarioService usuarioService;

    // ==========================
    // 1) Desbloquear usuario (ADMIN)
    // ==========================


    //Endpoint para que un ADMIN desbloquee a un usuario:
    //  - Quita el bloqueo por intentos fallidos de login.
    // - Quita el bloqueo por múltiples tokens inválidos/expirados.

    // POST /api/usuarios/{username}/desbloquear

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{username}/desbloquear")
    public ResponseEntity<Map<String, Object>> desbloquear(@PathVariable String username) {
        Map<String, Object> body = usuarioService.desbloquearUsuario(username);
        return ResponseEntity.ok(body);
    }

    // ==========================
    // 2) Crear usuarios por ADMIN
    // ==========================


    //Crea usuarios con rol ADMIN o USUARIO.
    //POST /api/usuarios/admin

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<Map<String, Object>> crearUsuarioPorAdmin(
            @Valid @RequestBody AdminCrearUsuarioRequest request
    ) {
        Map<String, Object> body = usuarioService.crearUsuarioPorAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // ==========================
    // 3) Listados de usuarios
    // ==========================


    //Lista TODOS los usuarios (ADMIN y USUARIO).
    //GET /api/usuarios

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodosLosUsuarios() {
        Map<String, Object> body = usuarioService.listarTodosLosUsuarios();
        return ResponseEntity.ok(body);
    }


      //Lista solo usuarios con rol ADMIN.
      //GET /api/usuarios/admins

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admins")
    public ResponseEntity<Map<String, Object>> listarUsuariosAdmin() {
        Map<String, Object> body = usuarioService.listarUsuariosPorRol("ADMIN");
        return ResponseEntity.ok(body);
    }


      //Lista solo usuarios con rol USUARIO
      //GET /api/usuarios/usuarios

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> listarUsuariosNormales() {
        Map<String, Object> body = usuarioService.listarUsuariosPorRol("USUARIO");
        return ResponseEntity.ok(body);
    }

    // ==========================
    // 4) Listar bloqueos
    // ==========================


      //Muestra los bloqueos activos por:
       //- intentos fallidos de login
      // - múltiples tokens inválidos/expirados

      //GET /api/usuarios/bloqueados

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bloqueados")
    public ResponseEntity<Map<String, Object>> listarBloqueos() {
        Map<String, Object> body = usuarioService.listarBloqueos();
        return ResponseEntity.ok(body);
    }

    // ==========================
    // 5) Actualizar MI cuenta (USUARIO / ADMIN)
    // ==========================



      //Permite que el propio usuario (USUARIO o ADMIN) actualice su cuenta.
      //PUT /api/usuarios/mi-cuenta

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/mi-cuenta")
    public ResponseEntity<UsuarioResponse> actualizarMiCuenta(
            Authentication auth,
            @Valid @RequestBody ActualizarMiCuentaRequest request
    ) {
        String usernameActual = auth.getName(); // viene del token
        UsuarioResponse response = usuarioService.actualizarMiCuenta(usernameActual, request);
        return ResponseEntity.ok(response);
    }

    // ==========================
    // 6) Actualizar usuario por ADMIN
    // ==========================


      //Permite al ADMIN actualizar los datos de cualquier usuario por id.

      //PUT /api/usuarios/{idUsuario}

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> actualizarUsuarioPorAdmin(
            @PathVariable Long idUsuario,
            @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        UsuarioResponse response = usuarioService.actualizarUsuarioPorAdmin(idUsuario, request);
        return ResponseEntity.ok(response);
    }

    // ==========================
    // 7) Eliminar usuario por ADMIN
    // ==========================


     // Permite al ADMIN eliminar una cuenta de usuario por id.

      // DELETE /api/usuarios/{idUsuario}

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> eliminarUsuarioPorAdmin(
            @PathVariable Long idUsuario
    ) {
        Map<String, Object> body = usuarioService.eliminarUsuarioPorAdmin(idUsuario);
        return ResponseEntity.ok(body);
    }
}
