package pe.edu.idat.biblioteca.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record UsuarioRequest(

        @NotBlank(message = "El DNI es obligatorio.")
        // ^\d{8}$ -> Debe empezar (^) y terminar ($) con exactamente 8 dígitos (\d{8}).
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

        @NotBlank(message = "El rol es obligatorio.")
        String rol,

        @NotBlank(message = "El número de teléfono es obligatorio.")
        // ^\d{9}$ -> Debe empezar (^) y terminar ($) con exactamente 9 dígitos (\d{9}).
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 números.")
        String telefono
) {}