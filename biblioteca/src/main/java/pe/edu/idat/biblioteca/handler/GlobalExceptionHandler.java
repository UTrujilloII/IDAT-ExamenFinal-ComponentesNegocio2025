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
    private record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {}
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
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResponseStatusException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleResponseStatusAndNotFoundExceptions(
            Exception ex) {

        HttpStatus status;
        String reason;

        if (ex instanceof ResponseStatusException rse) {
            status = (HttpStatus) rse.getStatusCode();
            reason = rse.getReason();
        } else if (ex instanceof NoSuchElementException nse) {
            status = HttpStatus.NOT_FOUND;
            reason = nse.getMessage() != null ? nse.getMessage() : "Recurso no encontrado.";
        } else {
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, String> error = new HashMap<>();
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
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}