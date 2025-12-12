package pe.edu.idat.msbiblioteca.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.msbiblioteca.dto.prestamo.DevolucionRequestDTO;
import pe.edu.idat.msbiblioteca.dto.prestamo.PrestamoRequestDTO;
import pe.edu.idat.msbiblioteca.dto.prestamo.PrestamoResponseDTO;
import pe.edu.idat.msbiblioteca.entity.Libro;
import pe.edu.idat.msbiblioteca.entity.Prestamo;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.exception.BusinessException;
import pe.edu.idat.msbiblioteca.exception.ResourceNotFoundException;
import pe.edu.idat.msbiblioteca.mappers.PrestamoMapper;
import pe.edu.idat.msbiblioteca.repository.LibroRepository;
import pe.edu.idat.msbiblioteca.repository.PrestamoRepository;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los préstamos de libros.
 * Maneja la creación, consulta, actualización y devolución de préstamos.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoMapper prestamoMapper;

    // Constantes de negocio
    private static final int MAX_PRESTAMOS_ACTIVOS = 5;
    private static final int DIAS_PRESTAMO_DEFAULT = 14;

    /**
     * Obtiene todos los préstamos registrados en el sistema.
     *
     * @return Lista de todos los préstamos como DTOs
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerTodosPrestamos() {
        log.info("Obteniendo todos los préstamos");
        return prestamoRepository.findAll()
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un préstamo por su ID.
     *
     * @param id ID del préstamo
     * @return DTO con los datos del préstamo
     * @throws ResourceNotFoundException si el préstamo no existe
     */
    @Transactional(readOnly = true)
    public PrestamoResponseDTO obtenerPrestamoPorId(Long id) {
        log.info("Obteniendo préstamo con ID: {}", id);
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));
        return prestamoMapper.toResponseDTO(prestamo);
    }

    /**
     * Obtiene todos los préstamos de un usuario específico.
     *
     * @param usuarioId ID del usuario
     * @return Lista de préstamos del usuario
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosPorUsuario(Long usuarioId) {
        log.info("Obteniendo préstamos del usuario ID: {}", usuarioId);
        return prestamoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los préstamos activos (no devueltos) de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Lista de préstamos activos del usuario
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosActivosPorUsuario(Long usuarioId) {
        log.info("Obteniendo préstamos activos del usuario ID: {}", usuarioId);
        return prestamoRepository.findPrestamosActivosByUsuario(usuarioId)
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los préstamos de un libro específico.
     *
     * @param libroId ID del libro
     * @return Lista de préstamos del libro
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosPorLibro(Long libroId) {
        log.info("Obteniendo préstamos del libro ID: {}", libroId);
        return prestamoRepository.findByLibroId(libroId)
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo préstamo de libro.
     * Valida disponibilidad del libro y límites del usuario.
     *
     * @param dto DTO con los datos del préstamo
     * @return DTO con los datos del préstamo creado
     * @throws ResourceNotFoundException si el usuario o libro no existen
     * @throws BusinessException si no se cumplen las reglas de negocio
     */
    @Transactional
    public PrestamoResponseDTO crearPrestamo(PrestamoRequestDTO dto) {
        log.info("Creando nuevo préstamo - Usuario ID: {}, Libro ID: {}",
                 dto.getUsuarioId(), dto.getLibroId());

        // Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + dto.getUsuarioId()));

        // Validar que el libro existe
        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: " + dto.getLibroId()));

        // Validar que el libro está disponible
        if (libro.getCopiasDisponibles() <= 0) {
            throw new BusinessException("El libro no tiene copias disponibles para préstamo");
        }

        // Validar que el usuario no tenga préstamos vencidos
        if (prestamoRepository.tienePrestamosVencidos(usuario.getId(), LocalDate.now())) {
            throw new BusinessException(
                    "El usuario tiene préstamos vencidos. Debe devolverlos antes de solicitar nuevos préstamos");
        }

        // Validar límite de préstamos activos
        Long prestamosActivos = prestamoRepository.contarPrestamosActivosByUsuario(usuario.getId());
        if (prestamosActivos >= MAX_PRESTAMOS_ACTIVOS) {
            throw new BusinessException(
                    "El usuario ha alcanzado el límite máximo de " + MAX_PRESTAMOS_ACTIVOS + " préstamos activos");
        }

        // Crear el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(dto.getFechaPrestamo() != null ? dto.getFechaPrestamo() : LocalDate.now());

        int diasPrestamo = dto.getDiasPrestamo() != null ? dto.getDiasPrestamo() : DIAS_PRESTAMO_DEFAULT;
        prestamo.setFechaDevolucionEsperada(prestamo.getFechaPrestamo().plusDays(diasPrestamo));

        prestamo.setEstado("ACTIVO");
        prestamo.setMulta(0.0);
        prestamo.setObservaciones(dto.getObservaciones());

        // Reducir copias disponibles del libro
        libro.prestar();
        libroRepository.save(libro);

        // Guardar préstamo
        prestamo = prestamoRepository.save(prestamo);

        log.info("Préstamo creado exitosamente con ID: {}", prestamo.getId());
        return prestamoMapper.toResponseDTO(prestamo);
    }

    /**
     * Registra la devolución de un libro prestado.
     * Calcula multas si hay retraso y actualiza disponibilidad del libro.
     *
     * @param id ID del préstamo a devolver
     * @param dto DTO con observaciones de devolución
     * @return DTO con los datos del préstamo actualizado
     * @throws ResourceNotFoundException si el préstamo no existe
     * @throws BusinessException si el préstamo ya fue devuelto
     */
    @Transactional
    public PrestamoResponseDTO registrarDevolucion(Long id, DevolucionRequestDTO dto) {
        log.info("Registrando devolución del préstamo ID: {}", id);

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        // Validar que el préstamo no esté ya devuelto
        if ("DEVUELTO".equals(prestamo.getEstado())) {
            throw new BusinessException("Este préstamo ya fue devuelto anteriormente");
        }

        // Registrar devolución
        prestamo.registrarDevolucion();

        // Agregar observaciones de devolución
        if (dto.getObservaciones() != null && !dto.getObservaciones().isEmpty()) {
            String observacionesActuales = prestamo.getObservaciones() != null ?
                    prestamo.getObservaciones() + " | " : "";
            prestamo.setObservaciones(observacionesActuales + "Devolución: " + dto.getObservaciones());
        }

        // Aumentar copias disponibles del libro
        Libro libro = prestamo.getLibro();
        libro.devolver();
        libroRepository.save(libro);

        prestamo = prestamoRepository.save(prestamo);

        log.info("Devolución registrada exitosamente. Multa: ${}", prestamo.getMulta());
        return prestamoMapper.toResponseDTO(prestamo);
    }

    /**
     * Obtiene todos los préstamos vencidos (fecha de devolución superada).
     *
     * @return Lista de préstamos vencidos
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosVencidos() {
        log.info("Obteniendo préstamos vencidos");
        return prestamoRepository.findPrestamosVencidos(LocalDate.now())
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los préstamos con multa pendiente.
     *
     * @return Lista de préstamos con multa
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerPrestamosConMulta() {
        log.info("Obteniendo préstamos con multa");
        return prestamoRepository.findPrestamosConMulta()
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de los préstamos, marcando como vencidos los que superaron
     * la fecha de devolución esperada y calculando sus multas.
     *
     * @return Número de préstamos actualizados
     */
    @Transactional
    public int actualizarEstadoPrestamos() {
        log.info("Actualizando estado de préstamos vencidos");

        List<Prestamo> prestamosVencidos = prestamoRepository.findPrestamosVencidos(LocalDate.now());

        prestamosVencidos.forEach(prestamo -> {
            prestamo.actualizarEstado();
            prestamoRepository.save(prestamo);
        });

        log.info("Se actualizaron {} préstamos vencidos", prestamosVencidos.size());
        return prestamosVencidos.size();
    }

    /**
     * Calcula el total de multas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Monto total de multas
     */
    @Transactional(readOnly = true)
    public Double calcularMultasUsuario(Long usuarioId) {
        log.info("Calculando multas del usuario ID: {}", usuarioId);
        return prestamoRepository.calcularMultasTotalesByUsuario(usuarioId);
    }

    /**
     * Obtiene estadísticas generales de préstamos.
     *
     * @return Arreglo con [total, activos, devueltos, vencidos]
     */
    @Transactional(readOnly = true)
    public Long[] obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de préstamos");

        List<Object[]> resultado = prestamoRepository.obtenerEstadisticasPrestamos();

        if (resultado.isEmpty()) {
            return new Long[]{0L, 0L, 0L, 0L};
        }

        Object[] stats = resultado.get(0);
        return new Long[]{
            ((Number) stats[0]).longValue(),  // Total
            ((Number) stats[1]).longValue(),  // Activos
            ((Number) stats[2]).longValue(),  // Devueltos
            ((Number) stats[3]).longValue()   // Vencidos
        };
    }

    /**
     * Obtiene los últimos N préstamos realizados.
     *
     * @param limit Número de préstamos a retornar
     * @return Lista de los últimos préstamos
     */
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerUltimosPrestamos(int limit) {
        log.info("Obteniendo últimos {} préstamos", limit);
        return prestamoRepository.findUltimosPrestamos(limit)
                .stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

