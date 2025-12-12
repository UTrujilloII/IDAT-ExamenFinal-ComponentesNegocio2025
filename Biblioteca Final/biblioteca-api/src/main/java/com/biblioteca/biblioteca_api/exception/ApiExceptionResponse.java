package com.biblioteca.biblioteca_api.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiExceptionResponse {
    private String mensaje;
    private String ruta;
    private LocalDateTime timestamp;
    private int status;
}
