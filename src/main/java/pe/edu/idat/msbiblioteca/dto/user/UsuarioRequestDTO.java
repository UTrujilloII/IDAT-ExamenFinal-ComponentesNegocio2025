package pe.edu.idat.msbiblioteca.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
    @NotBlank(message = "El username es obligatorio")
    @Size(max = 50)
    private String username;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password; // opcional en update

    @Size(max = 100)
    private String nombreCompleto;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String telefono;

    @Size(max = 200)
    private String direccion;

    private String role; // opcional (ADMIN, USUARIO o ROLE_ADMIN...)
}

