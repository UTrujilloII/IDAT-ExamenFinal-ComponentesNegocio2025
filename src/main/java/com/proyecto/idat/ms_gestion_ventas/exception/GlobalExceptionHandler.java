package com.proyecto.idat.ms_gestion_ventas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Pequeño DTO para detallar errores de validación por campo
    public record ValidationError(
            String field,
            String message
    ) {}

    // Estructura estándar que devolvemos en todos los errores
    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            List<ValidationError> errors
    ) {}

    // 1) Errores de validación (@Valid en los DTO de entrada)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationError(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Hay errores en los datos enviados",
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // 2) JSON mal formado / tipos de datos incorrectos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Cuerpo de la petición inválido",
                "El cuerpo de la petición no tiene el formato JSON correcto o el tipo de dato no es válido",
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // 3) Recurso no encontrado (404)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleNotFound(RecursoNoEncontradoException ex) {
        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // 4) Reglas de negocio
    //    Aquí llegan SOLO las ReglaNegocioException lanzadas desde la capa de servicio
    //    (stock, ISBN, préstamos, bloqueos de login, etc.), no las de token,
    //    porque las de token ya se manejan directamente en el JwtAuthenticationFilter.
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> handleBusiness(ReglaNegocioException ex) {

        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de negocio",
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // 5) Acceso denegado por roles (por ejemplo, rol USUARIO en endpoint solo ADMIN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {

        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),   // 403: autenticado pero sin permisos
                "Acceso denegado",
                "Acceso denegado: no tienes permisos para acceder a este recurso.",
                null
        );

        // Cualquier usuario autenticado que no tenga el rol requerido
        // (por ejemplo, un USUARIO tratando de entrar a un endpoint solo ADMIN)
        // recibirá SIEMPRE este mensaje claro.
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    // 6) Errores de integridad de datos (clave única, FK, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de integridad de datos",
                "Violación de restricciones de base de datos " +
                        "(por ejemplo, email, username o ISBN duplicado)",
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // 7) Fallback general para cualquier RuntimeException que no hayamos contemplado
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        ApiError apiError = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error inesperado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
