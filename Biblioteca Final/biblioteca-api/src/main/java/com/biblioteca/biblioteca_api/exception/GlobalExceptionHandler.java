package com.biblioteca.biblioteca_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🟥 Validación: 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validar(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));

        ApiError error = ApiError.builder()
                .status(400)
                .error("Error de validación")
                .message(errores.toString())
                .path("Validación de campos")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // 🟥 Errores de negocio: 400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {

        ApiError error = ApiError.builder()
                .status(400)
                .error("Solicitud inválida")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // 🟥 Cualquier otro error → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {

        ApiError error = ApiError.builder()
                .status(500)
                .error("Error interno del servidor")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return ResponseEntity.internalServerError().body(error);
    }
}
