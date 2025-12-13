package pe.idat.biblioteca.exeption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorException {
    private LocalDateTime fechaHora;
    private int status;
    private String error;
    private String mensaje;
    private String ruta;
}
