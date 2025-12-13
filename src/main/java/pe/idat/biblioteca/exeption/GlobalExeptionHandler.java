package pe.idat.biblioteca.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExeptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorException> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {

        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( m -> m.getField() + ": " + m.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorException obj = ErrorException.builder()
                .fechaHora(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Operacion Fallida")
                .mensaje(error)
                .ruta(request.getDescription(false))
                .build();
        return ResponseEntity.badRequest().body(obj);
    }
}
