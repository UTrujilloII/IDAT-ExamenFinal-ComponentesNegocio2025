package pe.edu.idat.msbiblioteca.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.msbiblioteca.dto.libro.LibroRequestDTO;
import pe.edu.idat.msbiblioteca.dto.libro.LibroResponseDTO;
import pe.edu.idat.msbiblioteca.entity.Libro;
import pe.edu.idat.msbiblioteca.exception.ResourceNotFoundException;
import pe.edu.idat.msbiblioteca.exception.BusinessException;
import pe.edu.idat.msbiblioteca.mappers.LibroMapper;
import pe.edu.idat.msbiblioteca.repository.LibroRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los libros.
 * Proporciona operaciones CRUD y consultas personalizadas.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    /**
     * Obtiene todos los libros registrados en el sistema.
     *
     * @return Lista de todos los libros como DTOs
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> obtenerTodosLosLibros() {
        log.info("Obteniendo todos los libros");
        return libroRepository.findAll()
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un libro por su ID.
     *
     * @param id ID del libro
     * @return DTO con los datos del libro
     * @throws ResourceNotFoundException si el libro no existe
     */
    @Transactional(readOnly = true)
    public LibroResponseDTO obtenerLibroPorId(Long id) {
        log.info("Obteniendo libro con ID: {}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        return libroMapper.toResponseDTO(libro);
    }

    /**
     * Obtiene un libro por su código ISBN.
     *
     * @param isbn Código ISBN del libro
     * @return DTO con los datos del libro
     * @throws ResourceNotFoundException si el libro no existe
     */
    @Transactional(readOnly = true)
    public LibroResponseDTO obtenerLibroPorIsbn(String isbn) {
        log.info("Obteniendo libro con ISBN: {}", isbn);
        Libro libro = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ISBN: " + isbn));
        return libroMapper.toResponseDTO(libro);
    }

    /**
     * Crea un nuevo libro en el sistema.
     * Valida que el ISBN no esté duplicado.
     *
     * @param dto DTO con los datos del libro a crear
     * @return DTO con los datos del libro creado
     * @throws BusinessException si el ISBN ya existe
     */
    @Transactional
    public LibroResponseDTO crearLibro(LibroRequestDTO dto) {
        log.info("Creando nuevo libro con ISBN: {}", dto.getIsbn());

        // Validar que el ISBN no exista
        if (libroRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new BusinessException("Ya existe un libro con el ISBN: " + dto.getIsbn());
        }

        Libro libro = libroMapper.toEntity(dto);

        // Si no se especifican copias disponibles, usar copias totales
        if (libro.getCopiasDisponibles() == null) {
            libro.setCopiasDisponibles(libro.getCopiasTotales());
        }

        libro = libroRepository.save(libro);
        log.info("Libro creado exitosamente con ID: {}", libro.getId());

        return libroMapper.toResponseDTO(libro);
    }

    /**
     * Actualiza un libro existente.
     * Solo actualiza los campos proporcionados en el DTO.
     *
     * @param id ID del libro a actualizar
     * @param dto DTO con los nuevos datos del libro
     * @return DTO con los datos del libro actualizado
     * @throws ResourceNotFoundException si el libro no existe
     * @throws BusinessException si se intenta cambiar a un ISBN ya existente
     */
    @Transactional
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO dto) {
        log.info("Actualizando libro con ID: {}", id);

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        // Si se está cambiando el ISBN, validar que no exista
        if (dto.getIsbn() != null && !dto.getIsbn().equals(libro.getIsbn())) {
            if (libroRepository.findByIsbn(dto.getIsbn()).isPresent()) {
                throw new BusinessException("Ya existe un libro con el ISBN: " + dto.getIsbn());
            }
        }

        libroMapper.updateEntityFromDTO(dto, libro);
        libro = libroRepository.save(libro);

        log.info("Libro actualizado exitosamente con ID: {}", id);
        return libroMapper.toResponseDTO(libro);
    }

    /**
     * Elimina un libro del sistema.
     * Solo se puede eliminar si no tiene préstamos activos.
     *
     * @param id ID del libro a eliminar
     * @throws ResourceNotFoundException si el libro no existe
     * @throws BusinessException si el libro tiene préstamos activos
     */
    @Transactional
    public void eliminarLibro(Long id) {
        log.info("Eliminando libro con ID: {}", id);

        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        // Validar que no tenga préstamos activos
        if (libro.getPrestamos() != null &&
            libro.getPrestamos().stream().anyMatch(p -> "ACTIVO".equals(p.getEstado()))) {
            throw new BusinessException("No se puede eliminar el libro porque tiene préstamos activos");
        }

        libroRepository.delete(libro);
        log.info("Libro eliminado exitosamente con ID: {}", id);
    }

    /**
     * Busca libros por título (búsqueda parcial, no sensible a mayúsculas).
     *
     * @param titulo Título o parte del título a buscar
     * @return Lista de libros que coinciden con la búsqueda
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarPorTitulo(String titulo) {
        log.info("Buscando libros por título: {}", titulo);
        return libroRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca libros por autor (búsqueda parcial, no sensible a mayúsculas).
     *
     * @param autor Autor o parte del nombre a buscar
     * @return Lista de libros del autor
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarPorAutor(String autor) {
        log.info("Buscando libros por autor: {}", autor);
        return libroRepository.findByAutorContainingIgnoreCase(autor)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca libros por categoría.
     *
     * @param categoria Categoría del libro
     * @return Lista de libros de la categoría
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarPorCategoria(String categoria) {
        log.info("Buscando libros por categoría: {}", categoria);
        return libroRepository.findByCategoria(categoria)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los libros disponibles para préstamo.
     *
     * @return Lista de libros disponibles
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> obtenerLibrosDisponibles() {
        log.info("Obteniendo libros disponibles");
        return libroRepository.findLibrosDisponibles()
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca libros por palabra clave (título, autor, ISBN, categoría).
     *
     * @param keyword Palabra clave para buscar
     * @return Lista de libros que coinciden con la búsqueda
     */
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> buscarLibros(String keyword) {
        log.info("Buscando libros con palabra clave: {}", keyword);
        return libroRepository.buscarLibros(keyword)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las categorías distintas de libros.
     *
     * @return Lista de categorías únicas
     */
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        log.info("Obteniendo todas las categorías");
        return libroRepository.findAllCategorias();
    }

    /**
     * Obtiene estadísticas de libros (total y disponibles).
     *
     * @return Arreglo con [total, disponibles]
     */
    @Transactional(readOnly = true)
    public Long[] obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de libros");
        Long total = libroRepository.contarTotalLibros();
        Long disponibles = libroRepository.contarLibrosDisponibles();
        return new Long[]{total, disponibles};
    }
}

