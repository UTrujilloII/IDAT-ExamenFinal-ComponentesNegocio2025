package com.biblioteca.biblioteca_api.services.impl;

import com.biblioteca.biblioteca_api.entity.*;
import com.biblioteca.biblioteca_api.repository.*;
import com.biblioteca.biblioteca_api.services.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    @Transactional
    @Override
    public Prestamo crearPrestamo(Long usuarioId, Long libroId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.getActivo()) {
            throw new RuntimeException("No se puede crear el préstamo: el libro está desactivado.");
        }

        if (libro.getDisponibles() <= 0) {
            throw new RuntimeException("No hay ejemplares disponibles");
        }

        libro.setDisponibles(libro.getDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = Prestamo.builder()
                .usuario(usuario)
                .libro(libro)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionEstimada(LocalDate.now().plusDays(7))
                .estado(EstadoPrestamo.PRESTADO)
                .build();

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    @Override
    public Prestamo devolverPrestamo(Long prestamoId) {

        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        prestamo.registrarDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.setDisponibles(libro.getDisponibles() + 1);
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> obtenerPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll(); // ✅ devuelve todos los préstamos
    }

    @Override
    public Prestamo obtenerPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
    }
}
