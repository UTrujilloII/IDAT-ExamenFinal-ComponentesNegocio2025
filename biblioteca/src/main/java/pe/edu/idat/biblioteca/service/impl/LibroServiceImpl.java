package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Añadir para transacciones
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.dto.libro.LibroPatchRequest; // ¡IMPORTANTE PARA EL MÉTODO PATCH!
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.EstadoPrestamo; // Necesario para la verificación
import pe.edu.idat.biblioteca.mappers.LibroMapper;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository; // Necesario para la eliminación segura
import pe.edu.idat.biblioteca.service.impl.LibroService; // Usar la interfaz

import java.util.List;
import java.util.NoSuchElementException; // Usar excepción específica
import java.util.Objects; // Necesario para la lógica PATCH y PUT
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Transacciones de solo lectura por defecto
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final PrestamoRepository prestamoRepository; // Necesaria para verificar préstamos

    // Método privado para obtener y manejar errores 404/NoSuchElement
    private Libro buscarLibroPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Libro no encontrado con ID: " + id));
    }

    @Override
    @Transactional // Sobrescribir para operaciones de escritura
    public LibroResponse registrarLibro(LibroRequest request) {
        if (libroRepository.existsByIsbn(request.isbn())) {
            throw new RuntimeException("El ISBN " + request.isbn() + " ya está registrado en la biblioteca.");
        }
        Libro nuevoLibro = libroMapper.toEntity(request);
        Libro libroGuardado = libroRepository.save(nuevoLibro);
        return libroMapper.toResponse(libroGuardado);
    }

    @Override
    public List<LibroResponse> listarTodos() {
        return libroRepository.findAll().stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LibroResponse obtenerLibro(Long id) {
        Libro libro = buscarLibroPorId(id);
        return libroMapper.toResponse(libro);
    }

    @Override
    @Transactional // Sobrescribir para operaciones de escritura
    public LibroResponse actualizarLibro(Long id, LibroRequest request) {
        Libro libroExistente = buscarLibroPorId(id);

        // Validación de unicidad de ISBN (excluyendo el libro actual)
        // NOTA: Si usas Spring Data JPA, el método ideal es existsByIsbnAndIdIsNot
        if (!libroExistente.getIsbn().equals(request.isbn()) &&
                libroRepository.existsByIsbnAndIdIsNot(request.isbn(), id)) {
            throw new RuntimeException("El ISBN " + request.isbn() + " ya está registrado en otro libro.");
        }
        // Si no cambias el ISBN o si el nuevo ISBN es único, procede la actualización

        Libro libroActualizado = libroMapper.updateEntity(request, libroExistente);

        return libroMapper.toResponse(libroRepository.save(libroActualizado));
    }

    // =========================================================================
    // IMPLEMENTACIÓN DEL MÉTODO PATCH (Actualización Parcial)
    // =========================================================================

    @Override
    @Transactional // Sobrescribir para operaciones de escritura
    public LibroResponse actualizarParcialLibro(Long id, LibroPatchRequest request) {

        Libro existingLibro = buscarLibroPorId(id);

        // 1. ISBN: Validar unicidad SOLO si el ISBN está presente Y ha cambiado
        if (request.isbn() != null && !request.isbn().isEmpty() &&
                !Objects.equals(existingLibro.getIsbn(), request.isbn())) {

            // Verifica si el ISBN ya existe en OTRO libro (ID diferente)
            if (libroRepository.existsByIsbnAndIdIsNot(request.isbn(), id)) {
                throw new RuntimeException("El ISBN ya está registrado para otro libro.");
            }
            existingLibro.setIsbn(request.isbn());
        }

        // 2. Título
        if (request.titulo() != null && !request.titulo().isEmpty()) {
            existingLibro.setTitulo(request.titulo());
        }

        // 3. Autor
        if (request.autor() != null && !request.autor().isEmpty()) {
            existingLibro.setAutor(request.autor());
        }

        // 4. Editorial
        if (request.editorial() != null && !request.editorial().isEmpty()) {
            existingLibro.setEditorial(request.editorial());
        }

        // 5. Año de Publicación (Tipo Integer, verificamos si es NULL)
        if (request.anioPublicacion() != null) {
            existingLibro.setAnioPublicacion(request.anioPublicacion());
        }

        // 6. Cantidad (Tipo Integer, verificamos si es NULL)
        if (request.cantidad() != null) {
            existingLibro.setCantidad(request.cantidad());
        }

        Libro saved = libroRepository.save(existingLibro);
        return libroMapper.toResponse(saved);
    }

    // =========================================================================
    // FINALIZACIÓN DE MÉTODOS
    // =========================================================================

    @Override
    @Transactional // Sobrescribir para operaciones de escritura
    public void eliminarLibro(Long id) {
        // Usa buscarLibroPorId para lanzar NoSuchElementException (404) si no existe
        Libro libroExistente = buscarLibroPorId(id);

        // --- LÓGICA DE NEGOCIO: Bloquear si hay préstamos ACTIVO o VENCIDO ---
        boolean tienePrestamosActivos = prestamoRepository.existsByLibroIdAndEstado(id, EstadoPrestamo.ACTIVO);
        boolean tienePrestamosVencidos = prestamoRepository.existsByLibroIdAndEstado(id, EstadoPrestamo.VENCIDO);

        if (tienePrestamosActivos || tienePrestamosVencidos) {
            throw new RuntimeException("El libro seleccionado no se puede eliminar porque tiene préstamos pendientes (ACTIVO o VENCIDO). Primero deben devolverse todas las copias.");
        }

        libroRepository.delete(libroExistente);
    }

    @Override
    public List<LibroResponse> listarDisponibles() {
        return libroRepository.findAllDisponibles().stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }
}