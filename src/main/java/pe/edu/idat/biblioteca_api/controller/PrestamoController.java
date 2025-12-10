package pe.edu.idat.biblioteca_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca_api.dto.PrestamoRequest;
import pe.edu.idat.biblioteca_api.model.Libro;
import pe.edu.idat.biblioteca_api.model.Prestamo;
import pe.edu.idat.biblioteca_api.model.Usuario;
import pe.edu.idat.biblioteca_api.repository.LibroRepository;
import pe.edu.idat.biblioteca_api.repository.PrestamoRepository;
import pe.edu.idat.biblioteca_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Préstamos", description = "Operaciones de préstamo y consulta de historial")
@SecurityRequirement(name = "bearerAuth") // <--- Aplica seguridad a todos los endpoints
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    // 1. Registrar préstamo (Solo ADMIN)
    // Nota: Agregamos esta restricción extra por si acaso, aunque SecurityConfig ya lo hace.
    @Operation(summary = "Registrar Préstamo", description = "Asigna un libro a un usuario y reduce el stock (Solo ADMIN).")
    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN')") 
    public ResponseEntity<?> registrarPrestamo(@Valid @RequestBody PrestamoRequest request) {
        
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (libro.getStock() <= 0) {
            return ResponseEntity.badRequest().body("No hay stock disponible para este libro.");
        }

        // Reducir stock
        libro.setStock(libro.getStock() - 1);
        libroRepository.save(libro);

        // Crear préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setEstado("ACTIVO");

        return ResponseEntity.ok(prestamoRepository.save(prestamo));
    }

    // 2. Ver mis préstamos (Solo USUARIO ve SU propio historial)
    @Operation(summary = "Ver mis préstamos", description = "Permite al usuario logueado ver su propio historial.")
    @GetMapping("/mis-prestamos")
    public ResponseEntity<List<Prestamo>> misPrestamos() {
        // Obtener el usuario autenticado del token
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(prestamoRepository.findByUsuarioId(usuario.getId()));
    }

    // 3. Registrar devolución (Solo ADMIN)
    // Actualiza el préstamo y devuelve el stock al libro
    @Operation(summary = "Registrar devolución", description = "Permite al administrador registrar la devolución de un libro.")
    @PutMapping("/devolver/{idPrestamo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> devolverLibro(@PathVariable Long idPrestamo) {
        
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if ("DEVUELTO".equals(prestamo.getEstado())) {
            return ResponseEntity.badRequest().body("Este préstamo ya fue devuelto anteriormente.");
        }

        // 1. Actualizar el Préstamo
        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucion(LocalDate.now());
        prestamoRepository.save(prestamo);

        // 2. Devolver Stock al Libro
        Libro libro = prestamo.getLibro();
        libro.setStock(libro.getStock() + 1);
        libroRepository.save(libro);

        return ResponseEntity.ok("Devolución registrada exitosamente. Stock actualizado.");
    }
}