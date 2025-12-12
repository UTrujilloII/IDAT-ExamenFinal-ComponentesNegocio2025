package com.biblioteca.api.repository;

import com.biblioteca.api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * =========================================================
 * REPOSITORIO LIBRO
 * Permite realizar operaciones CRUD sobre la tabla "libros".
 * Extiende JpaRepository, por lo que hereda métodos como:
 *  - save()
 *  - findById()
 *  - findAll()
 *  - deleteById()
 *  - count()
 * =========================================================
 */
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
