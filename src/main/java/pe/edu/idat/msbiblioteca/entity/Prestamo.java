package pe.edu.idat.msbiblioteca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entidad que representa un préstamo de libro en el sistema de biblioteca.
 * Gestiona la relación entre usuarios y libros prestados, incluyendo fechas y multas.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    /**
     * Identificador único del préstamo (clave primaria)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario que solicita el préstamo
     * Relación muchos a uno: muchos préstamos pertenecen a un usuario
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Libro que se presta
     * Relación muchos a uno: muchos préstamos pueden ser del mismo libro
     */
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    /**
     * Fecha en que se realizó el préstamo
     */
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    /**
     * Fecha límite para devolver el libro
     */
    @Column(name = "fecha_devolucion_esperada", nullable = false)
    private LocalDate fechaDevolucionEsperada;

    /**
     * Fecha real en que se devolvió el libro (null si aún no se ha devuelto)
     */
    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

    /**
     * Estado del préstamo: ACTIVO, DEVUELTO, VENCIDO
     */
    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO";

    /**
     * Monto de multa por retraso en la devolución
     */
    @Column(name = "multa")
    private Double multa = 0.0;

    /**
     * Observaciones adicionales sobre el préstamo
     */
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Calcula los días de retraso en la devolución del libro.
     * Si el libro no ha sido devuelto, calcula el retraso hasta la fecha actual.
     * Si ya fue devuelto, calcula el retraso hasta la fecha de devolución real.
     *
     * @return Número de días de retraso (0 si no hay retraso)
     */
    public long calcularDiasRetraso() {
        LocalDate fechaComparacion = fechaDevolucionReal != null ? fechaDevolucionReal : LocalDate.now();
        long dias = ChronoUnit.DAYS.between(fechaDevolucionEsperada, fechaComparacion);
        return dias > 0 ? dias : 0;
    }

    /**
     * Calcula la multa por retraso en la devolución.
     * Tarifa: $1.00 por día de retraso.
     *
     * @return Monto de la multa calculada
     */
    public double calcularMulta() {
        long diasRetraso = calcularDiasRetraso();
        return diasRetraso * 1.0; // $1.00 por día de retraso
    }

    /**
     * Actualiza el estado del préstamo a VENCIDO si la fecha actual
     * supera la fecha de devolución esperada y el libro no ha sido devuelto.
     */
    public void actualizarEstado() {
        if (fechaDevolucionReal == null && LocalDate.now().isAfter(fechaDevolucionEsperada)) {
            this.estado = "VENCIDO";
            this.multa = calcularMulta();
        }
    }

    /**
     * Registra la devolución del libro, actualizando estado y multa.
     */
    public void registrarDevolucion() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = "DEVUELTO";
        this.multa = calcularMulta();
    }
}

