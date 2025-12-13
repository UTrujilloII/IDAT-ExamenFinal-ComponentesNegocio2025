package com.proyecto.idat.ms_gestion_ventas.service.impl;

import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoResponse;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoUsuarioRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.ActualizarPrestamoAdminRequest;
import com.proyecto.idat.ms_gestion_ventas.entity.Libro;
import com.proyecto.idat.ms_gestion_ventas.entity.Prestamo;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import com.proyecto.idat.ms_gestion_ventas.exception.RecursoNoEncontradoException;
import com.proyecto.idat.ms_gestion_ventas.mappers.PrestamoMapper;
import com.proyecto.idat.ms_gestion_ventas.repository.LibroRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.PrestamoRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.UsuarioRepository;
import com.proyecto.idat.ms_gestion_ventas.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final PrestamoMapper prestamoMapper;

    // ----------------- ADMIN registra préstamo para cualquier usuario -----------------
    @Override
    public PrestamoResponse registrarPrestamo(PrestamoRequest request) {

        Usuario usuario = usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con id " + request.idUsuario()
                ));

        Libro libro = libroRepository.findById(request.idLibro())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + request.idLibro()
                ));

        if (libro.getEjemplaresDisponibles() == null || libro.getEjemplaresDisponibles() <= 0) {
            throw new ReglaNegocioException(
                    "No hay ejemplares disponibles para el libro '" + libro.getTitulo() +
                            "' (id " + libro.getIdLibro() + ")"
            );
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = request.fechaDevolucion();

        if (!fechaDevolucion.isAfter(fechaPrestamo)) {
            throw new ReglaNegocioException(
                    "La fecha de devolución (" + fechaDevolucion +
                            ") debe ser al menos un día después de la fecha de préstamo (" + fechaPrestamo + ")"
            );
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado("ACTIVO");

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro); // Se guarda el stock actualizado del libro (descuento por préstamo)

        Prestamo guardado = prestamoRepository.save(prestamo);

        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- USUARIO se presta un libro para SÍ MISMO -----------------
    @Override
    public PrestamoResponse registrarPrestamoUsuario(PrestamoUsuarioRequest request,
                                                     UserDetails usuarioActual) {

        Usuario usuario = usuarioRepository.findByUsername(usuarioActual.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con username " + usuarioActual.getUsername()
                ));

        Libro libro = libroRepository.findById(request.idLibro())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + request.idLibro()
                ));

        if (libro.getEjemplaresDisponibles() == null || libro.getEjemplaresDisponibles() <= 0) {
            throw new ReglaNegocioException(
                    "No hay ejemplares disponibles para el libro '" + libro.getTitulo() +
                            "' (id " + libro.getIdLibro() + ")"
            );
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = request.fechaDevolucion();

        if (!fechaDevolucion.isAfter(fechaPrestamo)) {
            throw new ReglaNegocioException(
                    "La fecha de devolución (" + fechaDevolucion +
                            ") debe ser al menos un día después de la fecha de préstamo (" + fechaPrestamo + ")"
            );
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado("ACTIVO");

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro); // Se guarda el stock actualizado del libro (descuento por préstamo del usuario)

        Prestamo guardado = prestamoRepository.save(prestamo);

        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- USUARIO devuelve UNO DE SUS préstamos -----------------
    @Override
    public PrestamoResponse devolverPrestamoUsuario(Long idPrestamo, UserDetails usuarioActual) {

        Usuario usuario = usuarioRepository.findByUsername(usuarioActual.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con username " + usuarioActual.getUsername()
                ));

        Prestamo prestamo = prestamoRepository.findByIdPrestamoAndUsuario(idPrestamo, usuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el préstamo con id " + idPrestamo +
                                " para el usuario " + usuario.getUsername()
                ));

        if (!"ACTIVO".equalsIgnoreCase(prestamo.getEstado())) { // solo se permite devolver si el préstamo está ACTIVO
            throw new ReglaNegocioException(
                    "Solo se puede devolver un préstamo con estado ACTIVO. Estado actual: " + prestamo.getEstado()
            ); // evita devolver préstamos DEVUELTO/CANCELADO/u otros estados
        }

        // se marca como devuelto
        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucion(LocalDate.now());

        // se devuelve al stock del libro
        Libro libro = prestamo.getLibro();

        if (libro.getEjemplaresDisponibles() >= libro.getEjemplaresTotales()) { //evita que disponibles supere el total
            throw new ReglaNegocioException(
                    "No se puede devolver el préstamo porque el stock disponible (" + libro.getEjemplaresDisponibles() +
                            ") ya es igual o mayor al total (" + libro.getEjemplaresTotales() + ") para el libro '" +
                            libro.getTitulo() + "' (id " + libro.getIdLibro() + ")"
            );
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro); // Se guarda el stock actualizado del libro (aumenta por devolución del usuario)

        Prestamo guardado = prestamoRepository.save(prestamo);

        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- ADMIN devuelve cualquier préstamo -----------------
    @Override
    public PrestamoResponse devolverPrestamoAdmin(Long idPrestamo) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el préstamo con id " + idPrestamo
                ));

        if (!"ACTIVO".equalsIgnoreCase(prestamo.getEstado())) { // solo se permite devolver si el préstamo está ACTIVO
            throw new ReglaNegocioException(
                    "Solo se puede devolver un préstamo con estado ACTIVO. Estado actual: " + prestamo.getEstado()
            ); // evita devolver préstamos DEVUELTO/CANCELADO/u otros estados
        }

        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();

        if (libro.getEjemplaresDisponibles() >= libro.getEjemplaresTotales()) { //  evita que disponibles supere el total
            throw new ReglaNegocioException(
                    "No se puede devolver el préstamo porque el stock disponible (" + libro.getEjemplaresDisponibles() +
                            ") ya es igual o mayor al total (" + libro.getEjemplaresTotales() + ") para el libro '" +
                            libro.getTitulo() + "' (id " + libro.getIdLibro() + ")"
            );
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro); // Se guarda el stock actualizado del libro (aumenta por devolución realizada por ADMIN)

        Prestamo guardado = prestamoRepository.save(prestamo);

        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- Historial del usuario autenticado -----------------
    @Override
    public List<PrestamoResponse> historialUsuario(UserDetails usuarioActual) {

        Usuario usuario = usuarioRepository.findByUsername(usuarioActual.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario con username " + usuarioActual.getUsername()
                ));

        List<Prestamo> prestamos = prestamoRepository.findByUsuario(usuario);

        return prestamoMapper.toResponseList(prestamos);
    }

    // ----------------- ADMIN: listar TODOS los préstamos -----------------
    @Override
    public List<PrestamoResponse> listarTodos() {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        return prestamoMapper.toResponseList(prestamos);
    }

    // ----------------- Actualizar préstamo del propio usuario -----------------
    @Override
    public PrestamoResponse actualizarPrestamoUsuario(Long idPrestamo,
                                                      String usernameActual,
                                                      ActualizarPrestamoUsuarioRequest request) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un préstamo con id " + idPrestamo
                ));

        if (!prestamo.getUsuario().getUsername().equals(usernameActual)) {
            throw new ReglaNegocioException("No puedes modificar un préstamo que no te pertenece");
        }

        if (!"ACTIVO".equalsIgnoreCase(prestamo.getEstado())) {
            throw new ReglaNegocioException("Solo se pueden modificar préstamos con estado ACTIVO");
        }

        if (!request.fechaDevolucion().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException(
                    "La fecha de devolución debe ser al menos un día después de la fecha actual"
            );
        }

        Libro libroAnterior = prestamo.getLibro();

        if (!libroAnterior.getIdLibro().equals(request.libroId())) {
            Libro nuevoLibro = libroRepository.findById(request.libroId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el libro con id " + request.libroId()
                    ));

            if (nuevoLibro.getEjemplaresDisponibles() == null
                    || nuevoLibro.getEjemplaresDisponibles() <= 0) {
                throw new ReglaNegocioException("No hay ejemplares disponibles para el nuevo libro seleccionado");
            }

            libroAnterior.setEjemplaresDisponibles(
                    libroAnterior.getEjemplaresDisponibles() + 1
            );

            nuevoLibro.setEjemplaresDisponibles(
                    nuevoLibro.getEjemplaresDisponibles() - 1
            );

            libroRepository.save(libroAnterior);
            libroRepository.save(nuevoLibro);

            prestamo.setLibro(nuevoLibro);
        }

        prestamo.setFechaDevolucion(request.fechaDevolucion());

        Prestamo guardado = prestamoRepository.save(prestamo);
        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- Actualizar préstamo como ADMIN -----------------
    @Override
    public PrestamoResponse actualizarPrestamoAdmin(Long idPrestamo,
                                                    ActualizarPrestamoAdminRequest request) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un préstamo con id " + idPrestamo
                ));

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un usuario con id " + request.usuarioId()
                ));

        if (!request.fechaDevolucion().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException(
                    "La fecha de devolución debe ser al menos un día después de la fecha actual"
            );
        }

        Libro libroAnterior = prestamo.getLibro();

        if (!libroAnterior.getIdLibro().equals(request.libroId())) {
            Libro nuevoLibro = libroRepository.findById(request.libroId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el libro con id " + request.libroId()
                    ));

            if (nuevoLibro.getEjemplaresDisponibles() == null
                    || nuevoLibro.getEjemplaresDisponibles() <= 0) {
                throw new ReglaNegocioException("No hay ejemplares disponibles para el nuevo libro seleccionado");
            }

            libroAnterior.setEjemplaresDisponibles(
                    libroAnterior.getEjemplaresDisponibles() + 1
            );

            nuevoLibro.setEjemplaresDisponibles(
                    nuevoLibro.getEjemplaresDisponibles() - 1
            );

            libroRepository.save(libroAnterior);
            libroRepository.save(nuevoLibro);

            prestamo.setLibro(nuevoLibro);
        }

        prestamo.setUsuario(usuario);
        prestamo.setFechaDevolucion(request.fechaDevolucion());

        Prestamo guardado = prestamoRepository.save(prestamo);
        return prestamoMapper.toResponse(guardado);
    }

    // ----------------- Eliminar préstamo del propio usuario -----------------
    @Override
    public void eliminarPrestamoUsuario(Long idPrestamo, String usernameActual) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un préstamo con id " + idPrestamo
                ));

        if (!prestamo.getUsuario().getUsername().equals(usernameActual)) {
            throw new ReglaNegocioException("No puedes eliminar un préstamo que no te pertenece");
        }

        if (!"ACTIVO".equalsIgnoreCase(prestamo.getEstado())) {
            throw new ReglaNegocioException("Solo se pueden cancelar préstamos con estado ACTIVO");
        }

        Libro libro = prestamo.getLibro();

        if (libro.getEjemplaresDisponibles() >= libro.getEjemplaresTotales()) { // evita superar el total al cancelar (también devuelve stock)
            throw new ReglaNegocioException(
                    "No se puede cancelar el préstamo porque el stock disponible (" + libro.getEjemplaresDisponibles() +
                            ") ya es igual o mayor al total (" + libro.getEjemplaresTotales() + ") para el libro '" +
                            libro.getTitulo() + "' (id " + libro.getIdLibro() + ")"
            );
        }

        libro.setEjemplaresDisponibles(
                libro.getEjemplaresDisponibles() + 1
        );
        libroRepository.save(libro);

        prestamoRepository.delete(prestamo);
    }

    // ----------------- Eliminar préstamo como ADMIN (usuario + préstamo) -----------------
    @Override
    public void eliminarPrestamoAdmin(Long idUsuario, Long idPrestamo) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un usuario con id " + idUsuario
                ));

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un préstamo con id " + idPrestamo
                ));

        if (!prestamo.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new ReglaNegocioException(
                    "El préstamo con id " + idPrestamo +
                            " no pertenece al usuario con id " + idUsuario
            );
        }

        if ("ACTIVO".equalsIgnoreCase(prestamo.getEstado())) {
            Libro libro = prestamo.getLibro();

            if (libro.getEjemplaresDisponibles() >= libro.getEjemplaresTotales()) { // evita superar el total al cancelar (admin)
                throw new ReglaNegocioException(
                        "No se puede cancelar el préstamo porque el stock disponible (" + libro.getEjemplaresDisponibles() +
                                ") ya es igual o mayor al total (" + libro.getEjemplaresTotales() + ") para el libro '" +
                                libro.getTitulo() + "' (id " + libro.getIdLibro() + ")"
                );
            }

            libro.setEjemplaresDisponibles(
                    libro.getEjemplaresDisponibles() + 1
            );
            libroRepository.save(libro);
        }

        prestamoRepository.delete(prestamo);
    }
}
