package com.biblioteca.biblioteca_api.entity;

import lombok.Getter;

@Getter
public enum EstadoPrestamo {

    PRESTADO("Libro en préstamo"),
    DEVUELTO("Libro devuelto"),
    ATRASADO("Préstamo atrasado");

    private final String descripcion;

    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }

}
