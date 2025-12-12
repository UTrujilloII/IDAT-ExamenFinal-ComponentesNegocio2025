package com.biblioteca.biblioteca_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    private String autor;

    @Column(unique = true)
    private String isbn;

    @Min(value = 1, message = "Debe haber al menos 1 ejemplar")
    @Column(nullable = false)
    private Integer totalEjemplares = 1;

    @Min(value = 0, message = "Los ejemplares disponibles no pueden ser negativos")
    @Column(nullable = false)
    private Integer disponibles = 1;

    @Builder.Default
    @Column(nullable = false)            // esto agregue
    private Boolean activo = true;


    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Prestamo> prestamos = new ArrayList<>();




    // ---------------------------------------
    // MÉTODOS UTILES (Opcional pero recomendado)
    // ---------------------------------------

    /** Reduce la cantidad de libros disponibles en 1 */
    public boolean prestarEjemplar() {
        if (this.disponibles > 0) {
            this.disponibles--;
            return true;
        }
        return false;
    }

    /** Incrementa la cantidad de libros disponibles al devolverlos */
    public void devolverEjemplar() {
        if (this.disponibles < this.totalEjemplares) {
            this.disponibles++;
        }
    }
}
