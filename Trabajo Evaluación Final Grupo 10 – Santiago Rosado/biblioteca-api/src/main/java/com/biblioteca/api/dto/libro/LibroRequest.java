package com.biblioteca.api.dto.libro;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibroRequest {
    private String titulo;
    private String autor;
    private int anio;
}
