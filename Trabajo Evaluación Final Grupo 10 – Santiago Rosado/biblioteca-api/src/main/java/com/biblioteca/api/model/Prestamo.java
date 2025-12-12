package com.biblioteca.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * =========================================================
 * ENTIDAD PRÉSTAMO
 * Representa el proceso de prestar un libro a un usuario.
 *
 * Relaciones:
 *  - Muchos Préstamos pertenecen a un Usuario.
 *  - Muchos Préstamos pertenecen a un Libro.
 * =========================================================
 */
@Entity
@Table(name = "prestamos")
@Getter @Setter
@NoArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;

    @Column(nullable = false, length = 20)
    private String estado; // "ACTIVO" o "DEVUELTO"

    // ========= RELACIÓN MANY-TO-ONE CON USUARIO =========
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // ========= RELACIÓN MANY-TO-ONE CON LIBRO =========
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
}
