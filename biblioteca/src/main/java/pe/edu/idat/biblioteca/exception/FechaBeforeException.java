package pe.edu.idat.biblioteca.exception;

public class FechaBeforeException extends RuntimeException {
    public FechaBeforeException(String message) {
        super(message);
    }
}
