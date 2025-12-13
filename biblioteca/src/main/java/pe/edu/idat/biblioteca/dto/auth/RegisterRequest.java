package pe.edu.idat.biblioteca.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 números.")
        String dni,

        @NotBlank(message = "El nombre es obligatorio y no debe estar vacío.")
        @Size(min = 3, max = 50, message = "El nombre debe contener entre 3 y 50 caracteres.")
        String nombre,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "El formato del email es inválido. Asegúrese de incluir '@'.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password,

        @NotBlank(message = "El número de teléfono es obligatorio.")
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 números.")
        String telefono,
        @NotBlank(message = "El rol es obligatorio.")
        String role
) {}