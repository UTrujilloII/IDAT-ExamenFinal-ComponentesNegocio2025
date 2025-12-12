package pe.edu.idat.biblioteca.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.service.impl.PrestamoService;
import pe.edu.idat.biblioteca.service.impl.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/v1/prestamos") // Usaremos '/v1' como prefijo estándar
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService; // Necesario para buscar el ID de usuario

    // --- 1. REGISTRAR PRÉSTAMO ---
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo Admin registra préstamos
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody PrestamoRequest request) {
        return ResponseEntity.ok(prestamoService.crearPrestamo(request));
    }

    // --- 2. REGISTRAR DEVOLUCIÓN ---
    // Usamos el ID del préstamo directamente en la ruta
    @PostMapping("/devolucion/{prestamoId}")
    @PreAuthorize("hasRole('ADMIN')") // Solo Admin registra devoluciones
    public ResponseEntity<PrestamoResponse> registrarDevolucion(@PathVariable Long prestamoId) {
        // El servicio espera el ID del préstamo
        return ResponseEntity.ok(prestamoService.registrarDevolucion(prestamoId));
    }

    // --- 3. LISTAR TODOS (Historial General) ---
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo Admin ve todo el historial
    public ResponseEntity<List<PrestamoResponse>> listarPrestamos() {
        // Usaremos el método que lista todos los préstamos para el historial general
        return ResponseEntity.ok(prestamoService.listarPrestamosActivos());
    }

    // --- 4. PRÉSTAMOS DEL USUARIO LOGUEADO ---
    // El usuario puede ver su propio historial (USER o ADMIN)
    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PrestamoResponse>> historialUsuario(Authentication authentication) {
        // CRÍTICO: Debemos obtener el ID del usuario a partir del email (authentication.getName())

        // 1. Obtener el ID del Usuario logueado usando el email (username)
        // Se asume que UsuarioService tiene un método para buscar por email y obtener el ID
        String userEmail = authentication.getName();

        // **NOTA: Necesitas un método en UsuarioService para obtener el ID/Usuario por email**
        // Por ahora, usamos una búsqueda para demostrar la funcionalidad:
        Long usuarioId = usuarioService.obtenerUsuarioPorEmail(userEmail).id();

        // 2. Llamar al servicio con el ID
        return ResponseEntity.ok(prestamoService.listarHistorialUsuario(usuarioId));
    }
}