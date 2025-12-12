package pe.edu.idat.msbiblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.msbiblioteca.dto.common.ApiResponse;
import pe.edu.idat.msbiblioteca.dto.prestamo.DevolucionRequestDTO;
import pe.edu.idat.msbiblioteca.dto.prestamo.PrestamoRequestDTO;
import pe.edu.idat.msbiblioteca.dto.prestamo.PrestamoResponseDTO;
import pe.edu.idat.msbiblioteca.service.PrestamoService;

import java.util.List;

/**
 * Controlador REST para la gestión de préstamos de libros.
 * Proporciona endpoints para operaciones CRUD y consultas de préstamos.
 *
 * Seguridad:
 * - ADMIN: Acceso total a todos los préstamos y operaciones
 * - USUARIO: Solo puede ver y gestionar sus propios préstamos
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@RestController
@RequestMapping("/v1/prestamos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PrestamoController {

    private final PrestamoService prestamoService;

    /**
     * Obtiene todos los préstamos del sistema.
     * Solo accesible para ADMIN.
     *
     * @return ResponseEntity con lista de todos los préstamos
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerTodosPrestamos() {
        log.info("GET /api/prestamos - Obteniendo todos los préstamos");

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerTodosPrestamos();

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Obtiene un préstamo por su ID.
     * ADMIN puede ver cualquier préstamo.
     * USUARIO solo puede ver sus propios préstamos (validado en el servicio).
     *
     * @param id ID del préstamo
     * @return ResponseEntity con datos del préstamo
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> obtenerPrestamoPorId(@PathVariable Long id) {
        log.info("GET /api/prestamos/{} - Obteniendo préstamo por ID", id);

        PrestamoResponseDTO prestamo = prestamoService.obtenerPrestamoPorId(id);

        return ResponseEntity.ok(ApiResponse.<PrestamoResponseDTO>builder()
                .success(true)
                .message("Préstamo encontrado")
                .data(prestamo)
                .build());
    }

    /**
     * Obtiene todos los préstamos de un usuario específico.
     * ADMIN puede ver préstamos de cualquier usuario.
     * USUARIO solo puede ver sus propios préstamos.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con lista de préstamos del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerPrestamosPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/prestamos/usuario/{} - Obteniendo préstamos del usuario", usuarioId);

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerPrestamosPorUsuario(usuarioId);

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos del usuario obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Obtiene los préstamos activos de un usuario.
     * ADMIN puede ver préstamos de cualquier usuario.
     * USUARIO solo puede ver sus propios préstamos activos.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con lista de préstamos activos
     */
    @GetMapping("/usuario/{usuarioId}/activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerPrestamosActivosPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/prestamos/usuario/{}/activos - Obteniendo préstamos activos", usuarioId);

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerPrestamosActivosPorUsuario(usuarioId);

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos activos obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Obtiene mis préstamos (del usuario autenticado).
     * Accesible para USUARIO.
     *
     * @return ResponseEntity con lista de préstamos del usuario autenticado
     */
    @GetMapping("/mis-prestamos")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerMisPrestamos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("GET /api/prestamos/mis-prestamos - Usuario: {}", username);

        // Nota: Este endpoint requeriría obtener el ID del usuario desde el username
        // Por simplicidad, se puede usar el endpoint /usuario/{usuarioId} desde el frontend

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Use el endpoint /usuario/{usuarioId} con su ID de usuario")
                .build());
    }

    /**
     * Obtiene todos los préstamos de un libro específico.
     * Accesible para ADMIN.
     *
     * @param libroId ID del libro
     * @return ResponseEntity con lista de préstamos del libro
     */
    @GetMapping("/libro/{libroId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerPrestamosPorLibro(
            @PathVariable Long libroId) {
        log.info("GET /api/prestamos/libro/{} - Obteniendo préstamos del libro", libroId);

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerPrestamosPorLibro(libroId);

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos del libro obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Crea un nuevo préstamo de libro.
     * ADMIN puede crear préstamos para cualquier usuario.
     *
     * @param dto DTO con datos del préstamo
     * @return ResponseEntity con el préstamo creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> crearPrestamo(
            @Valid @RequestBody PrestamoRequestDTO dto) {
        log.info("POST /api/prestamos - Creando nuevo préstamo");

        PrestamoResponseDTO prestamo = prestamoService.crearPrestamo(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PrestamoResponseDTO>builder()
                        .success(true)
                        .message("Préstamo creado exitosamente")
                        .data(prestamo)
                        .build());
    }

    /**
     * Registra la devolución de un libro prestado.
     * ADMIN puede registrar devoluciones de cualquier préstamo.
     *
     * @param id ID del préstamo
     * @param dto DTO con observaciones de devolución
     * @return ResponseEntity con el préstamo actualizado
     */
    @PutMapping("/{id}/devolver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> registrarDevolucion(
            @PathVariable Long id,
            @RequestBody(required = false) DevolucionRequestDTO dto) {
        log.info("PUT /api/prestamos/{}/devolver - Registrando devolución", id);

        if (dto == null) {
            dto = new DevolucionRequestDTO();
        }

        PrestamoResponseDTO prestamo = prestamoService.registrarDevolucion(id, dto);

        return ResponseEntity.ok(ApiResponse.<PrestamoResponseDTO>builder()
                .success(true)
                .message("Devolución registrada exitosamente. Multa: $" + prestamo.getMulta())
                .data(prestamo)
                .build());
    }

    /**
     * Obtiene todos los préstamos vencidos.
     * Solo accesible para ADMIN.
     *
     * @return ResponseEntity con lista de préstamos vencidos
     */
    @GetMapping("/vencidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerPrestamosVencidos() {
        log.info("GET /api/prestamos/vencidos - Obteniendo préstamos vencidos");

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerPrestamosVencidos();

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos vencidos obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Obtiene todos los préstamos con multa pendiente.
     * Solo accesible para ADMIN.
     *
     * @return ResponseEntity con lista de préstamos con multa
     */
    @GetMapping("/con-multa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerPrestamosConMulta() {
        log.info("GET /api/prestamos/con-multa - Obteniendo préstamos con multa");

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerPrestamosConMulta();

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Préstamos con multa obtenidos exitosamente")
                .data(prestamos)
                .build());
    }

    /**
     * Actualiza el estado de los préstamos (marca como vencidos).
     * Solo accesible para ADMIN.
     * Útil para ejecutar manualmente o mediante un job programado.
     *
     * @return ResponseEntity con número de préstamos actualizados
     */
    @PostMapping("/actualizar-estados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> actualizarEstadoPrestamos() {
        log.info("POST /api/prestamos/actualizar-estados - Actualizando estados");

        int actualizados = prestamoService.actualizarEstadoPrestamos();

        return ResponseEntity.ok(ApiResponse.<Integer>builder()
                .success(true)
                .message("Préstamos actualizados: " + actualizados)
                .data(actualizados)
                .build());
    }

    /**
     * Calcula el total de multas de un usuario.
     * ADMIN puede ver multas de cualquier usuario.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con el total de multas
     */
    @GetMapping("/usuario/{usuarioId}/multas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Double>> calcularMultasUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/prestamos/usuario/{}/multas - Calculando multas", usuarioId);

        Double multas = prestamoService.calcularMultasUsuario(usuarioId);

        return ResponseEntity.ok(ApiResponse.<Double>builder()
                .success(true)
                .message("Total de multas del usuario: $" + multas)
                .data(multas)
                .build());
    }

    /**
     * Obtiene estadísticas generales de préstamos.
     * Solo accesible para ADMIN.
     *
     * @return ResponseEntity con estadísticas [total, activos, devueltos, vencidos]
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long[]>> obtenerEstadisticas() {
        log.info("GET /api/prestamos/estadisticas - Obteniendo estadísticas");

        Long[] stats = prestamoService.obtenerEstadisticas();

        return ResponseEntity.ok(ApiResponse.<Long[]>builder()
                .success(true)
                .message("Estadísticas: [Total: " + stats[0] + ", Activos: " + stats[1] +
                        ", Devueltos: " + stats[2] + ", Vencidos: " + stats[3] + "]")
                .data(stats)
                .build());
    }

    /**
     * Obtiene los últimos N préstamos realizados.
     * Solo accesible para ADMIN.
     *
     * @param limit Número de préstamos a retornar (por defecto 10)
     * @return ResponseEntity con lista de últimos préstamos
     */
    @GetMapping("/ultimos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> obtenerUltimosPrestamos(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /v1/prestamos/ultimos?limit={}", limit);

        List<PrestamoResponseDTO> prestamos = prestamoService.obtenerUltimosPrestamos(limit);

        return ResponseEntity.ok(ApiResponse.<List<PrestamoResponseDTO>>builder()
                .success(true)
                .message("Últimos préstamos obtenidos exitosamente")
                .data(prestamos)
                .build());
    }
}

