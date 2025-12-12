package com.biblioteca.biblioteca_api.repository;

import com.biblioteca.biblioteca_api.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByActivoTrue(); //esto agregue

    List<Libro> findAll();


    boolean existsByIsbn(String isbn);



}
