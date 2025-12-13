package pe.edu.idat.biblioteca.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, length = 255)
    private String autor;

    @Column(length = 150)
    private String editorial;

    @Column(length = 100)
    private String categoria;

    @Column(name = "cantidad_total", nullable = false)
    private Integer cantidadTotal = 1;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @Column(nullable = false)
    private Boolean activo = true;


    //Joins
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prestamo> prestamos = new ArrayList<>();

}
