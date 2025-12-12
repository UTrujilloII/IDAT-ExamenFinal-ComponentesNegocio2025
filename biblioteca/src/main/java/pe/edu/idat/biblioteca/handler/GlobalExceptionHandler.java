package pe.edu.idat.biblioteca.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Clase interna para estandarizar el formato de error para errores 4xx lanzados desde el Service
    private record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {}

    /**
     * 1. Maneja ERRORES DE VALIDACIÓN (lanzados por @Valid, @NotBlank, @Email, etc.).
     * Devuelve HTTP 400 Bad Request y lista los campos con sus mensajes de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                // Para errores a nivel de objeto (si los hay)
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        // Este formato es el más útil para la validación de DTO
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * 2. Maneja ERRORES DE LÓGICA DE NEGOCIO Y RECURSOS NO ENCONTRADOS (400, 409, 404).
     * Captura excepciones lanzadas manualmente desde la capa de servicio usando throw new ResponseStatusException(...).
     */
    @ExceptionHandler({ResponseStatusException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleResponseStatusAndNotFoundExceptions(
            Exception ex) {

        HttpStatus status;
        String reason;

        if (ex instanceof ResponseStatusException rse) {
            // Maneja 400, 409, etc. lanzados desde el Service (ej. validación de fechas, stock)
            status = (HttpStatus) rse.getStatusCode();
            reason = rse.getReason();
        } else if (ex instanceof NoSuchElementException nse) {
            // Maneja 404 NOT_FOUND (ej. findById().orElseThrow())
            status = HttpStatus.NOT_FOUND;
            reason = nse.getMessage() != null ? nse.getMessage() : "Recurso no encontrado.";
        } else {
            // Fallback: Aunque es mejor lanzar ResponseStatusException, cubrimos otros Runtime.
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            reason = "Un error inesperado ocurrió en el servidor.";
        }

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                reason
        );

        return new ResponseEntity<>(response, status);
    }

    /**
     * 3. Maneja ERRORES DE INTEGRIDAD DE DATOS (BD) - ÚTIL PARA DUPLICIDAD.
     * Devuelve HTTP 409 Conflict.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, String> error = new HashMap<>();
        // Obtiene el mensaje original de la BD para hacer el diagnóstico
        String rootCause = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        String customMessage;

        if (rootCause != null && rootCause.contains("foreign key constraint fails")) {
            customMessage = "Error de integridad: No se puede eliminar o modificar el recurso. Existen dependencias que lo referencian (ej. un usuario con préstamos activos).";
        } else if (rootCause != null && rootCause.contains("Duplicate entry")) {
            // Esto cubre Duplicidad de DNI, email, ISBN, etc.
            customMessage = "Error de duplicidad: Ya existe un registro con un valor único (DNI, email, ISBN, etc.) que has intentado ingresar.";
        } else {
            customMessage = "Error de integridad de datos desconocido. Consulte al administrador.";
        }

        error.put("error", customMessage);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409 Conflict
    }
}