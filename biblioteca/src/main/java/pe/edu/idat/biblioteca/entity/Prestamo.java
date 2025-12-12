package pe.edu.idat.biblioteca.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Long idPrestamo;

    @Column(name = "fecha_prestamo",nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_entrega",nullable = false)
    private LocalDate fechaEntrega;

    @Column(name = "fecha_entregada",nullable = true)
    private LocalDate fechaEntregada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro",nullable = false)
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario",nullable = false)
    private Usuario usuario;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;
}
