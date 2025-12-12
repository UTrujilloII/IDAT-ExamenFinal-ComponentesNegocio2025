package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.mappers.PrestamoMapper;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoMapper prestamoMapper;

    @Override
    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));

        Libro libro = libroRepository.findById(request.libroId())
                .orElseThrow(() -> new NoSuchElementException("Libro no encontrado."));

        // Validación de Stock (usa el campo corregido cantidadDisponible)
        if (libro.getCantidadDisponible() <= 0) {
            throw new RuntimeException("El libro no tiene copias disponibles para préstamo.");
        }

        // Creación del Préstamo
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setUsuario(usuario);
        nuevoPrestamo.setLibro(libro);
        nuevoPrestamo.setFechaDevolucionEsperada(request.fechaDevolucionEsperada());

        // Actualización de Stock (CRÍTICO)
        libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
        libroRepository.save(libro);

        Prestamo savedPrestamo = prestamoRepository.save(nuevoPrestamo);
        return prestamoMapper.toResponse(savedPrestamo);
    }

    @Override
    @Transactional
    public PrestamoResponse registrarDevolucion(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no encontrado."));

        if (prestamo.getEstado() != Prestamo.EstadoPrestamo.ACTIVO && prestamo.getEstado() != Prestamo.EstadoPrestamo.VENCIDO) {
            throw new RuntimeException("El préstamo ya ha sido devuelto o cancelado.");
        }

        // Actualización del Préstamo
        prestamo.setFechaDevolucionReal(LocalDateTime.now());
        prestamo.setEstado(Prestamo.EstadoPrestamo.DEVUELTO);

        // Actualización de Stock
        Libro libro = prestamo.getLibro();
        libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
        libroRepository.save(libro);

        Prestamo updatedPrestamo = prestamoRepository.save(prestamo);
        return prestamoMapper.toResponse(updatedPrestamo);
    }

    @Override
    public List<PrestamoResponse> listarHistorialUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId).stream()
                .map(prestamoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> listarPrestamosActivos() {
        return prestamoRepository.findByEstado(Prestamo.EstadoPrestamo.ACTIVO).stream()
                .map(prestamoMapper::toResponse)
                .collect(Collectors.toList());
    }
}