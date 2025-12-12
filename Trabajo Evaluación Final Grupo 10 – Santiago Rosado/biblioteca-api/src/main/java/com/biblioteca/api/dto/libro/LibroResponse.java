package com.biblioteca.api.dto.libro;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibroResponse {
    private Long id;
    private String titulo;
    private String autor;
    private int anio;
}
