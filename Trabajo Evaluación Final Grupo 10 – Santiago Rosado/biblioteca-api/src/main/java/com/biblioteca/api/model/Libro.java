package com.biblioteca.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =========================================================
 * ENTIDAD LIBRO
 * Representa los libros disponibles en la biblioteca.
 *
 * Un libro puede tener múltiples préstamos asociados.
 * =========================================================
 */
@Entity
@Table(name = "libros")
@Getter @Setter
@NoArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 120)
    private String autor;

    @Column(nullable = false)
    private int anioPublicacion;

    @Column(nullable = false)
    private int cantidadDisponible;
}
