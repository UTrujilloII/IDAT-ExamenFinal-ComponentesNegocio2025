package pe.edu.idat.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long idLibro;

    @Column(name = "isbn",length =20 , unique = true)
    private String isbn;

    @Column(name = "titulo",length = 80,nullable = false)
    private String titulo;

    @Column(name = "descripcion",length = 250)
    private String descripcion;

    @Column(name = "autor",length = 50,nullable = false)
    private String autor;

    @Column(name = "genero",length = 30)
    private String genero;

    @Column(name = "stock")
    @Min(0)
    private Integer stock;

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;
}
