package pe.edu.idat.ms_biblioteca.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.idat.ms_biblioteca.dto.PrestamoDTO;
import pe.edu.idat.ms_biblioteca.entity.Libro;
import pe.edu.idat.ms_biblioteca.entity.Prestamo;
import pe.edu.idat.ms_biblioteca.entity.Usuario;
import pe.edu.idat.ms_biblioteca.repository.LibroRepository;
import pe.edu.idat.ms_biblioteca.repository.PrestamoRepository;
import pe.edu.idat.ms_biblioteca.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    // Método para registrar un nuevo préstamo
    @Transactional
    public Prestamo crearPrestamo(PrestamoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Libro libro = libroRepository.findById(dto.idLibro())
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (libro.getStockDisponible() <= 0) {
            throw new RuntimeException("No hay stock disponible");
        }

        // Descontamos stock
        libro.setStockDisponible(libro.getStockDisponible() - 1);
        libroRepository.save(libro);

        // Guardamos el préstamo
        Prestamo p = new Prestamo();
        p.setUsuario(usuario);
        p.setLibro(libro);
        p.setFechaPrestamo(dto.fechaPrestamo());
        p.setFechaDevolucion(dto.fechaDevolucion());
        p.setDevuelto(false);

        return prestamoRepository.save(p);
    }

    // Método para listar préstamos de un usuario
    public List<Prestamo> listarPorUsuario(Long idUsuario) {
        return prestamoRepository.findByUsuarioId(idUsuario);
    }

    // Método para devolver libro y recuperar stock
    public Prestamo devolverPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Solo devolvemos si no ha sido devuelto ya
        if (!prestamo.isDevuelto()) {
            prestamo.setDevuelto(true); // Cambiamos estado
            prestamo.setFechaDevolucion(LocalDate.now()); // Fecha actual

            // Recuperamos stock del libro
            Libro libro = prestamo.getLibro();
            libro.setStockDisponible(libro.getStockDisponible() + 1);
            libroRepository.save(libro);

            return prestamoRepository.save(prestamo);
        }
        return prestamo;
    }
}