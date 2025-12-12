package pe.edu.idat.msbiblioteca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa un libro en el sistema de biblioteca.
 * Contiene información del libro como título, autor, ISBN, categoría, etc.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    /**
     * Identificador único del libro (clave primaria)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código ISBN del libro - debe ser único en el sistema
     */
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    /**
     * Título del libro
     */
    @Column(nullable = false, length = 200)
    private String titulo;

    /**
     * Autor del libro
     */
    @Column(nullable = false, length = 150)
    private String autor;

    /**
     * Editorial que publicó el libro
     */
    @Column(length = 150)
    private String editorial;

    /**
     * Año de publicación del libro
     */
    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    /**
     * Categoría o género del libro (Ficción, No Ficción, Ciencia, etc.)
     */
    @Column(length = 100)
    private String categoria;

    /**
     * Cantidad de copias disponibles del libro
     */
    @Column(name = "copias_disponibles", nullable = false)
    private Integer copiasDisponibles = 0;

    /**
     * Cantidad total de copias del libro en la biblioteca
     */
    @Column(name = "copias_totales", nullable = false)
    private Integer copiasTotales = 0;

    /**
     * Descripción o sinopsis del libro
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Ubicación física del libro en la biblioteca (estante, sección)
     */
    @Column(length = 50)
    private String ubicacion;

    /**
     * Estado del libro: DISPONIBLE, AGOTADO, EN_MANTENIMIENTO
     */
    @Column(nullable = false, length = 20)
    private String estado = "DISPONIBLE";

    /**
     * Fecha de registro del libro en el sistema
     */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    /**
     * Lista de préstamos asociados a este libro
     * Relación uno a muchos: un libro puede tener muchos préstamos
     */
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Prestamo> prestamos;

    /**
     * Reduce la cantidad de copias disponibles cuando se presta un libro
     */
    public void prestar() {
        if (this.copiasDisponibles > 0) {
            this.copiasDisponibles--;
            if (this.copiasDisponibles == 0) {
                this.estado = "AGOTADO";
            }
        }
    }

    /**
     * Aumenta la cantidad de copias disponibles cuando se devuelve un libro
     */
    public void devolver() {
        if (this.copiasDisponibles < this.copiasTotales) {
            this.copiasDisponibles++;
            if (this.copiasDisponibles > 0) {
                this.estado = "DISPONIBLE";
            }
        }
    }
}

