package pe.edu.idat.msbiblioteca.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private boolean enabled;
    private LocalDate fechaRegistro;
    private Set<String> roles;
}

