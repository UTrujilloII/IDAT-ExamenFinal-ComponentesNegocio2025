package com.proyecto.idat.ms_gestion_ventas.repository;

import com.proyecto.idat.ms_gestion_ventas.entity.Libro;
import com.proyecto.idat.ms_gestion_ventas.entity.Prestamo;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuario(Usuario usuario);

    // Para listar sólo los prestamos pendientes del usuario
    List<Prestamo> findByUsuarioAndEstado(Usuario usuario, String estado);

    // Para devolver validando que el préstamo es del usuario
    Optional<Prestamo> findByIdPrestamoAndUsuario(Long idPrestamo, Usuario usuario);

    // saber si un libro está siendo usado en algún prEstamo
    boolean existsByLibro(Libro libro);
}
