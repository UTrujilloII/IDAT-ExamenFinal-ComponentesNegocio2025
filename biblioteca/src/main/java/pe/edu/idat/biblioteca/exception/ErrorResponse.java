package pe.edu.idat.biblioteca.exception;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        LocalDate fechaError,
        Integer status,
        String error,
        String mensaje,
        String path,
        List<Map<String, String>> errores
) {
}
