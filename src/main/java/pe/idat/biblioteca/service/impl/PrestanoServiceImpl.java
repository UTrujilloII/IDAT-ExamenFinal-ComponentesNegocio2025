package pe.idat.biblioteca.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.idat.biblioteca.entity.Libro;
import pe.idat.biblioteca.entity.Prestamo;
import pe.idat.biblioteca.entity.Usuario;
import pe.idat.biblioteca.repository.LibroRepository;
import pe.idat.biblioteca.repository.PrestamoRepository;
import pe.idat.biblioteca.repository.UsuarioRepository;
import pe.idat.biblioteca.security.UserDetailServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestanoServiceImpl implements PrestamoService{

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final UserDetailServiceImpl userDetailService;
    @Override
    public PrestamoResponse registarPrestamo(PrestamoRequest prestamoRequest) {

        Usuario usuario = usuarioRepository.findById(prestamoRequest.idUsuario())
                .orElseThrow(() -> new RuntimeException("No existe el usuario ingresado"));
        Libro libro = libroRepository.findById(prestamoRequest.idLibro())
                .orElseThrow(() -> new RuntimeException("El libro ingresado no existe "));

        if (libro.getStock() <= 0) {
            throw new RuntimeException("El libro con ID " + libro.getId() + " no tiene unidades disponibles para préstamo.");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(prestamoRequest.fechaPrestamo());
        prestamo.setFechaDevolucion(prestamoRequest.fechaDevolucion());
        prestamo.setDevuelto(false);
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        libro.setStock(libro.getStock()-1);
        libroRepository.save(libro);

        return new PrestamoResponse(
                prestamoGuardado.getId(),
                prestamoGuardado.getFechaPrestamo(),
                prestamoGuardado.getFechaDevolucion(),
                prestamoGuardado.isDevuelto(),
                usuario.getId(),
                usuario.getUsername(),
                libro.getId(),
                libro.getTitulo()
        );
    }

    @Transactional
    @Override
    public void registarDevolucion(Long idprestamo) {

        Prestamo prestamo = prestamoRepository.findById(idprestamo)
                .orElseThrow(() -> new RuntimeException("El Préstamo con ID " + idprestamo + " no fue encontrado."));
        if(prestamo.isDevuelto()){
            throw new RuntimeException("Este préstamo ya fue marcado como devuelto.");
        }

        Libro libro = prestamo.getLibro();
        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setDevuelto(true);
        prestamoRepository.save(prestamo);

        libro.setStock(libro.getStock()+1);
        libroRepository.save(libro);

    }

    @Override
    public List<PrestamoResponse> obtenerMisPrestamosPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuario.getId());

        return prestamos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrestamoResponse> obtenerTodosPrestamos() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        return prestamos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private PrestamoResponse convertirAResponse(Prestamo prestamo) {
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.isDevuelto(),
                prestamo.getUsuario().getId(),
                prestamo.getUsuario().getUsername(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo()
        );
    }

}
