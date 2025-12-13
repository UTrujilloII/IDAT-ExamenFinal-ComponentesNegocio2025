package com.proyecto.idat.ms_gestion_ventas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;

    @Column(nullable = false)
    private String titulo;

    private String autor;

    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;

    private LocalDate fechaPublicacion;

    private Integer ejemplaresTotales;

    @Column(nullable = false) //  evita que ejemplaresDisponibles sea null en BD (stock siempre numérico)
    private Integer ejemplaresDisponibles;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;
}
