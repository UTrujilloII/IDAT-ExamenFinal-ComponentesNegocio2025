package com.proyecto.idat.ms_gestion_ventas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    private String nombre;

    // para navegar desde Categoria hacia sYs libros
    @OneToMany(mappedBy = "categoria")
    private List<Libro> libros;
}
