package com.biblioteca.biblioteca_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------------------------------------------------------
    // RELACIONES
    // ------------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "libro_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Libro libro;

    // ------------------------------------------------------------
    // CAMPOS DE FECHA
    // ------------------------------------------------------------

    @NotNull(message = "La fecha de préstamo es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucionEstimada;

    private LocalDate fechaDevolucionReal;

    // ------------------------------------------------------------
    // ESTADO DEL PRÉSTAMO
    // ------------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estado;

    // ------------------------------------------------------------
    // MÉTODOS ÚTILES (Opcional)
    // ------------------------------------------------------------

    /**
     * Marca el préstamo como devuelto y registra la fecha de devolución.
     */
    public void registrarDevolucion(LocalDate fecha) {
        this.fechaDevolucionReal = fecha;
        this.estado = EstadoPrestamo.DEVUELTO;
    }

    /**
     * Define estado inicial si no viene en el builder.
     */
    @PrePersist
    private void prePersist() {
        if (estado == null) {
            estado = EstadoPrestamo.PRESTADO;
        }
    }
}
