package pe.edu.idat.biblioteca.dto.usuario; // Misma ubicación que UsuarioRequest.java

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record UsuarioPatchRequest(
        // DNI: Opcional, pero si viene, debe tener 8 dígitos.
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 números.")
        String dni,

        // Nombre: Opcional.
        @Size(min = 3, max = 50, message = "El nombre debe contener entre 3 y 50 caracteres.")
        String nombre,

        // Email: Opcional, pero si viene, debe ser válido.
        @Email(message = "El formato del email es inválido. Asegúrese de incluir '@'.")
        String email,

        // Contraseña: Opcional.
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password,

        // Rol: Opcional.
        String rol,

        // Teléfono: Opcional, pero si viene, debe tener 9 dígitos.
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 números.")
        String telefono
) {}