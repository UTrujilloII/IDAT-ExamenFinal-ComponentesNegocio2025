package pe.edu.idat.msbiblioteca.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.msbiblioteca.dto.common.ApiResponse;
import pe.edu.idat.msbiblioteca.dto.libro.LibroRequestDTO;
import pe.edu.idat.msbiblioteca.dto.libro.LibroResponseDTO;
import pe.edu.idat.msbiblioteca.service.LibroService;

import java.util.List;

/**
 * Controlador REST para la gestión de libros en el sistema de biblioteca.
 * Proporciona endpoints para operaciones CRUD y consultas de libros.
 *
 * Seguridad:
 * - ADMIN: Puede crear, actualizar y eliminar libros
 * - USUARIO: Solo puede consultar libros
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@RestController
@RequestMapping("/v1/libros")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class LibroController {

    private final LibroService libroService;

    /**
     * Obtiene todos los libros registrados.
     * Accesible para ADMIN y USUARIO.
     *
     * @return ResponseEntity con lista de libros
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> obtenerTodosLosLibros() {
        log.info("GET /v1/libros - Obteniendo todos los libros");

        List<LibroResponseDTO> libros = libroService.obtenerTodosLosLibros();

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Libros obtenidos exitosamente")
                .data(libros)
                .build());
    }

    /**
     * Obtiene un libro por su ID.
     * Accesible para ADMIN y USUARIO.
     *
     * @param id ID del libro
     * @return ResponseEntity con datos del libro
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<LibroResponseDTO>> obtenerLibroPorId(@PathVariable Long id) {
        log.info("GET /api/libros/{} - Obteniendo libro por ID", id);

        LibroResponseDTO libro = libroService.obtenerLibroPorId(id);

        return ResponseEntity.ok(ApiResponse.<LibroResponseDTO>builder()
                .success(true)
                .message("Libro encontrado")
                .data(libro)
                .build());
    }

    /**
     * Obtiene un libro por su código ISBN.
     * Accesible para ADMIN y USUARIO.
     *
     * @param isbn Código ISBN del libro
     * @return ResponseEntity con datos del libro
     */
    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<LibroResponseDTO>> obtenerLibroPorIsbn(@PathVariable String isbn) {
        log.info("GET /api/libros/isbn/{} - Obteniendo libro por ISBN", isbn);

        LibroResponseDTO libro = libroService.obtenerLibroPorIsbn(isbn);

        return ResponseEntity.ok(ApiResponse.<LibroResponseDTO>builder()
                .success(true)
                .message("Libro encontrado")
                .data(libro)
                .build());
    }

    /**
     * Crea un nuevo libro en el sistema.
     * Solo accesible para ADMIN.
     *
     * @param dto DTO con datos del libro a crear
     * @return ResponseEntity con el libro creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LibroResponseDTO>> crearLibro(@Valid @RequestBody LibroRequestDTO dto) {
        log.info("POST /api/libros - Creando nuevo libro: {}", dto.getTitulo());

        LibroResponseDTO libro = libroService.crearLibro(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LibroResponseDTO>builder()
                        .success(true)
                        .message("Libro creado exitosamente")
                        .data(libro)
                        .build());
    }

    /**
     * Actualiza un libro existente.
     * Solo accesible para ADMIN.
     *
     * @param id ID del libro a actualizar
     * @param dto DTO con los nuevos datos del libro
     * @return ResponseEntity con el libro actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LibroResponseDTO>> actualizarLibro(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequestDTO dto) {
        log.info("PUT /api/libros/{} - Actualizando libro", id);

        LibroResponseDTO libro = libroService.actualizarLibro(id, dto);

        return ResponseEntity.ok(ApiResponse.<LibroResponseDTO>builder()
                .success(true)
                .message("Libro actualizado exitosamente")
                .data(libro)
                .build());
    }

    /**
     * Elimina un libro del sistema.
     * Solo accesible para ADMIN.
     *
     * @param id ID del libro a eliminar
     * @return ResponseEntity con confirmación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminarLibro(@PathVariable Long id) {
        log.info("DELETE /api/libros/{} - Eliminando libro", id);

        libroService.eliminarLibro(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Libro eliminado exitosamente")
                .build());
    }

    /**
     * Busca libros por título.
     * Accesible para ADMIN y USUARIO.
     *
     * @param titulo Título o parte del título a buscar
     * @return ResponseEntity con lista de libros encontrados
     */
    @GetMapping("/buscar/titulo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> buscarPorTitulo(
            @RequestParam String titulo) {
        log.info("GET /api/libros/buscar/titulo?titulo={}", titulo);

        List<LibroResponseDTO> libros = libroService.buscarPorTitulo(titulo);

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Búsqueda completada")
                .data(libros)
                .build());
    }

    /**
     * Busca libros por autor.
     * Accesible para ADMIN y USUARIO.
     *
     * @param autor Nombre del autor o parte del nombre
     * @return ResponseEntity con lista de libros encontrados
     */
    @GetMapping("/buscar/autor")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> buscarPorAutor(
            @RequestParam String autor) {
        log.info("GET /api/libros/buscar/autor?autor={}", autor);

        List<LibroResponseDTO> libros = libroService.buscarPorAutor(autor);

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Búsqueda completada")
                .data(libros)
                .build());
    }

    /**
     * Busca libros por categoría.
     * Accesible para ADMIN y USUARIO.
     *
     * @param categoria Categoría del libro
     * @return ResponseEntity con lista de libros encontrados
     */
    @GetMapping("/buscar/categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> buscarPorCategoria(
            @RequestParam String categoria) {
        log.info("GET /api/libros/buscar/categoria?categoria={}", categoria);

        List<LibroResponseDTO> libros = libroService.buscarPorCategoria(categoria);

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Búsqueda completada")
                .data(libros)
                .build());
    }

    /**
     * Obtiene todos los libros disponibles para préstamo.
     * Accesible para ADMIN y USUARIO.
     *
     * @return ResponseEntity con lista de libros disponibles
     */
    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> obtenerLibrosDisponibles() {
        log.info("GET /api/libros/disponibles - Obteniendo libros disponibles");

        List<LibroResponseDTO> libros = libroService.obtenerLibrosDisponibles();

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Libros disponibles obtenidos exitosamente")
                .data(libros)
                .build());
    }

    /**
     * Busca libros por palabra clave (título, autor, ISBN, categoría).
     * Accesible para ADMIN y USUARIO.
     *
     * @param keyword Palabra clave para buscar
     * @return ResponseEntity con lista de libros encontrados
     */
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<LibroResponseDTO>>> buscarLibros(
            @RequestParam String keyword) {
        log.info("GET /api/libros/buscar?keyword={}", keyword);

        List<LibroResponseDTO> libros = libroService.buscarLibros(keyword);

        return ResponseEntity.ok(ApiResponse.<List<LibroResponseDTO>>builder()
                .success(true)
                .message("Búsqueda completada")
                .data(libros)
                .build());
    }

    /**
     * Obtiene todas las categorías distintas de libros.
     * Accesible para ADMIN y USUARIO.
     *
     * @return ResponseEntity con lista de categorías
     */
    @GetMapping("/categorias")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<ApiResponse<List<String>>> obtenerCategorias() {
        log.info("GET /api/libros/categorias - Obteniendo categorías");

        List<String> categorias = libroService.obtenerCategorias();

        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                .success(true)
                .message("Categorías obtenidas exitosamente")
                .data(categorias)
                .build());
    }

    /**
     * Obtiene estadísticas de libros (total y disponibles).
     * Accesible para ADMIN.
     *
     * @return ResponseEntity con estadísticas
     */
    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long[]>> obtenerEstadisticas() {
        log.info("GET /v1/libros/estadisticas - Obteniendo estadísticas");

        Long[] stats = libroService.obtenerEstadisticas();

        return ResponseEntity.ok(ApiResponse.<Long[]>builder()
                .success(true)
                .message("Estadísticas obtenidas: [Total: " + stats[0] + ", Disponibles: " + stats[1] + "]")
                .data(stats)
                .build());
    }
}
