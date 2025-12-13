package pe.idat.biblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.idat.biblioteca.service.impl.PrestamoService;

import java.util.List;

@RestController
@RequestMapping("v1/prestamos")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public ResponseEntity<PrestamoResponse> registrarPrestamo(@Valid @RequestBody PrestamoRequest prestamoRequest){
        PrestamoResponse prestamoResponse = prestamoService.registarPrestamo(prestamoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idprestamo}")
    public ResponseEntity<Void> registarDevolucion(@PathVariable Long idprestamo){
        prestamoService.registarDevolucion(idprestamo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<PrestamoResponse>> obtenerMisPrestamos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<PrestamoResponse> prestamos = prestamoService.obtenerMisPrestamosPorUsername(username);
        return ResponseEntity.ok(prestamos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> obtenerTodosPrestamos() {
        List<PrestamoResponse> prestamos = prestamoService.obtenerTodosPrestamos();
        return ResponseEntity.ok(prestamos);
    }


}
