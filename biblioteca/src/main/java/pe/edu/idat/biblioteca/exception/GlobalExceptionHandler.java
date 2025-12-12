package pe.edu.idat.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales incorrectas"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,String>> handleAccessDenied(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "No tienes permisos para acceder a este endpoint"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        List<Map<String,String>> errores=ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e-> Map.of(
                        "campo",e.getField(),
                        "mensaje",e.getDefaultMessage()
                ))
                .toList();
        ErrorResponse showError= new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "campos invalidos",
                "se encontraron errores de validacion",
                request.getDescription(false).replace("uri=",""),
                errores

        );
        return new ResponseEntity<>(showError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRefreshException.class)
    public ResponseEntity<ErrorResponse> handlerInvalidRefreshException(InvalidRefreshException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "token invalido",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUsuarioNotFoundException(UsuarioNotFoundException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "usuario error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibroAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerLibroAlreadyExistsException(LibroAlreadyExistsException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "error de usuario ",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibroNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerLibroNotFoundException(LibroNotFoundException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "libro no encontrado",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handlerStockInsuficienteException(StockInsuficienteException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "Stock error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(PrestamoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerPrestamoNotFoundException(PrestamoNotFoundException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "prestamo error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerRolNotFoundException(RolNotFoundException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.NOT_FOUND.value(),
                "rol error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(RolAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlerRolAlreadyExistsException(RolAlreadyExistsException ex,WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.CONFLICT.value(),
                "rol error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FechaBeforeException.class)
    public ResponseEntity<ErrorResponse>handlerFechaBeforeException(FechaBeforeException ex , WebRequest request){
        ErrorResponse showError=new ErrorResponse(
                LocalDate.now(),
                HttpStatus.BAD_REQUEST.value(),
                "fecha entrega Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=",""),
                null
        );
        return new ResponseEntity<>(showError,HttpStatus.BAD_REQUEST);
    }



}
