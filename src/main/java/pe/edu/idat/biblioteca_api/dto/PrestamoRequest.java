package pe.edu.idat.biblioteca_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PrestamoRequest {
    private Long usuarioId; // El alumno que se lleva el libro
    private Long libroId;   // El libro que se lleva
}