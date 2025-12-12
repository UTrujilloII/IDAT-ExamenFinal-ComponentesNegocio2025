package com.proyecto.idat.ms_gestion_ventas.controller;

import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoResponse;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoAdminRequest;
import com.proyecto.idat.ms_gestion_ventas.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    // ADMIN registra préstamo para cualquier usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PrestamoResponse> registrar(@Valid @RequestBody PrestamoRequest request) {
        return ResponseEntity.ok(prestamoService.registrarPrestamo(request));
    }

    // USUARIO se presta libro para sí mismo
    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping("/mis-prestamos")
    public ResponseEntity<PrestamoResponse> registrarParaUsuario(
            @Valid @RequestBody PrestamoUsuarioRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                prestamoService.registrarPrestamoUsuario(request, userDetails)
        );
    }

    // USUARIO devuelve uno de sus préstamos
    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping("/mis-prestamos/{idPrestamo}/devolver")
    public ResponseEntity<PrestamoResponse> devolverUsuario(
            @PathVariable Long idPrestamo,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                prestamoService.devolverPrestamoUsuario(idPrestamo, userDetails)
        );
    }

    // ADMIN marca como devuelto cualquier préstamo
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idPrestamo}/devolver")
    public ResponseEntity<PrestamoResponse> devolverAdmin(@PathVariable Long idPrestamo) {
        return ResponseEntity.ok(prestamoService.devolverPrestamoAdmin(idPrestamo));
    }

    // USUARIO ve solo su historial
    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<PrestamoResponse>> historial(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(prestamoService.historialUsuario(userDetails));
    }

    // ADMIN ve todos los préstamos
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> listarTodos() {
        return ResponseEntity.ok(prestamoService.listarTodos());
    }

    // USUARIO actualiza uno de sus préstamos (solo si está ACTIVO)
    @PreAuthorize("hasRole('USUARIO')")
    @PutMapping("/mis-prestamos/{idPrestamo}")
    public ResponseEntity<PrestamoResponse> actualizarMiPrestamo(
            @PathVariable Long idPrestamo,
            Authentication auth,
            @Valid @RequestBody ActualizarPrestamoUsuarioRequest request
    ) {
        String username = auth.getName();
        PrestamoResponse response = prestamoService.actualizarPrestamoUsuario(idPrestamo, username, request);
        return ResponseEntity.ok(response);
    }

    // USUARIO cancela / elimina uno de sus préstamos (solo si está ACTIVO)
    @PreAuthorize("hasRole('USUARIO')")
    @DeleteMapping("/mis-prestamos/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> cancelarMiPrestamo(
            @PathVariable Long idPrestamo,
            Authentication auth
    ) {
        String username = auth.getName();
        prestamoService.eliminarPrestamoUsuario(idPrestamo, username);

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Préstamo eliminado correctamente");
        body.put("idPrestamoEliminado", idPrestamo);
        body.put("accion", "CANCELACION_PROPIA");

        return ResponseEntity.ok(body);
    }


    // ADMIN actualiza préstamo de cualquier usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idPrestamo}")
    public ResponseEntity<PrestamoResponse> actualizarPrestamoComoAdmin(
            @PathVariable Long idPrestamo,
            @Valid @RequestBody ActualizarPrestamoAdminRequest request
    ) {
        PrestamoResponse response = prestamoService.actualizarPrestamoAdmin(idPrestamo, request);
        return ResponseEntity.ok(response);
    }

    // ADMIN elimina préstamo de un usuario (envías idUsuario + idPrestamo)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/usuarios/{idUsuario}/prestamos/{idPrestamo}")
    public ResponseEntity<Map<String, Object>> eliminarPrestamoComoAdmin(
            @PathVariable Long idUsuario,
            @PathVariable Long idPrestamo
    ) {
        prestamoService.eliminarPrestamoAdmin(idUsuario, idPrestamo);

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Préstamo eliminado correctamente por ADMIN");
        body.put("idUsuario", idUsuario);
        body.put("idPrestamo", idPrestamo);

        return ResponseEntity.ok(body);
    }
}
