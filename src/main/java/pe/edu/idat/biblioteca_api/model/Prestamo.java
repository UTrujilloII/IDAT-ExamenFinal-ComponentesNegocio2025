package pe.edu.idat.biblioteca_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Getter @Setter
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_prestamo")
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion; // Puede ser nulo si aún no lo devuelve

    @Column(name = "estado")
    private String estado; // "ACTIVO", "DEVUELTO"

    // Relación Muchos a Uno: Un usuario tiene muchos préstamos
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación Muchos a Uno: Un libro puede estar en muchos préstamos (histórico)
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;
}