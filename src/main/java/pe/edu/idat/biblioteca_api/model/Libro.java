package pe.edu.idat.biblioteca_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "libros")
@Getter @Setter
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Debe especificar el año de publicación")
    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private int stock;
}