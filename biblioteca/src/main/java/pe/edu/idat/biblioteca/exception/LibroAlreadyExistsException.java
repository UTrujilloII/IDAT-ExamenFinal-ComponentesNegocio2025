package pe.edu.idat.biblioteca.exception;

public class LibroAlreadyExistsException extends RuntimeException {
    public LibroAlreadyExistsException(String message) {
        super(message);
    }
}
