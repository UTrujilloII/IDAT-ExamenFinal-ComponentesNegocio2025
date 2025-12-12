package com.biblioteca.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Clase utilizada para estructurar los errores de manera uniforme.
 * Esto mejora la respuesta enviada al cliente en caso de fallos.
 */
@Getter
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
}

