package pe.edu.idat.biblioteca.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "libro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String titulo;

    @Column(length = 100, nullable = false)
    private String autor;

    @Column(length = 100, nullable = false)
    private String editorial;

    @Column(length = 19, unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer anioPublicacion;

    @Column(nullable = false)
    private Integer cantidad;
}