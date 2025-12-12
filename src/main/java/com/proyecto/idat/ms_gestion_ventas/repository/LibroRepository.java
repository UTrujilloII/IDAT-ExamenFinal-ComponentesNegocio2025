package com.proyecto.idat.ms_gestion_ventas.repository;

import com.proyecto.idat.ms_gestion_ventas.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByIsbn(String isbn);
}
