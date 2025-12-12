package pe.edu.idat.biblioteca.dto.rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolRequest (
        @NotBlank(message = "el campo nombre es obligatorio")
        @Size(min = 2 ,max = 25 ,message = "el nombre debe tener entre 2 y 25 caracteres")
        String nombre
){}
