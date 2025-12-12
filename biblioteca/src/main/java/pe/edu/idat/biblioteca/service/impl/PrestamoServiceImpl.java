package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoItemRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.entity.EstadoPrestamo;
import pe.edu.idat.biblioteca.mappers.PrestamoMapper;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;
import pe.edu.idat.biblioteca.service.impl.PrestamoService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final PrestamoMapper prestamoMapper;
    private static final int LIMITE_PRESTAMOS = 5;

    @Override
    public List<PrestamoResponse> crearPrestamo(PrestamoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + request.usuarioId()));
        LocalDate fechaPrestamo = (request.fechaPrestamo() != null) ?
                request.fechaPrestamo() :
                LocalDate.now();

        LocalDate fechaDevolucionEstimada = request.fechaDevolucionEstimada();
        if (fechaDevolucionEstimada.isBefore(fechaPrestamo)) {
            String mensajeError = String.format(
                    "La fecha de devolución estimada (%s) no puede ser anterior a la fecha de préstamo (%s).",
                    fechaDevolucionEstimada, fechaPrestamo
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensajeError);
        }
        int totalItemsSolicitados = request.items().stream()
                .mapToInt(PrestamoItemRequest::cantidad)
                .sum();
        long prestamosActivos = prestamoRepository.countByUsuarioAndEstadoActivo(usuario);

        if ((prestamosActivos + totalItemsSolicitados) > LIMITE_PRESTAMOS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario ha excedido el límite. Tiene " + prestamosActivos + " préstamos activos. Máximo permitido: " + LIMITE_PRESTAMOS
            );
        }
        List<Prestamo> prestamosCreados = new ArrayList<>();

        for (PrestamoItemRequest item : request.items()) {

            Libro libro = libroRepository.findById(item.libroId())
                    .orElseThrow(() -> new NoSuchElementException("Libro no encontrado con ID: " + item.libroId()));
            if (libro.getCantidad() < item.cantidad()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para el libro '" + libro.getTitulo() + "'. Solicitados: " + item.cantidad() + ", Disponibles: " + libro.getCantidad()
                );
            }
            for (int i = 0; i < item.cantidad(); i++) {
                Prestamo prestamo = new Prestamo();
                prestamo.setUsuario(usuario);
                prestamo.setLibro(libro);
                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setFechaDevolucionEstimada(fechaDevolucionEstimada);
                prestamo.setEstado(EstadoPrestamo.ACTIVO);

                prestamosCreados.add(prestamo);
            }
            libro.setCantidad(libro.getCantidad() - item.cantidad());
            libroRepository.save(libro);
        }
        List<Prestamo> prestamosGuardados = prestamoRepository.saveAll(prestamosCreados);
        return prestamosGuardados.stream()
                .map(prestamoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void devolverPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado con ID: " + id));
        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El préstamo ya ha sido devuelto anteriormente.");
        }
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamoRepository.save(prestamo);

        Libro libro = prestamo.getLibro();
        libro.setCantidad(libro.getCantidad() + 1);
        libroRepository.save(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerHistorial(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + username));
        return prestamoRepository.findByUsuarioOrderByFechaPrestamoDesc(usuario)
                .stream()
                .map(prestamoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponse obtenerPrestamoPorId(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado con ID: " + id));
        return prestamoMapper.toResponse(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> listarTodosLosPrestamos() {
        return prestamoRepository.findAll().stream()
                .map(prestamoMapper::toResponse)
                .collect(Collectors.toList());
    }
}