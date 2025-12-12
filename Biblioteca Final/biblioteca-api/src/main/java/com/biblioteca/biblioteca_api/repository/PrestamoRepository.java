package com.biblioteca.biblioteca_api.repository;

import com.biblioteca.biblioteca_api.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByLibroId(Long libroId);

}
