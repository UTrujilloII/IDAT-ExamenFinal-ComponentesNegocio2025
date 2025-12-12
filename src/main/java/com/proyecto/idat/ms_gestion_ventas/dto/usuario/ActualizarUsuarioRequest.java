package com.proyecto.idat.ms_gestion_ventas.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


 // DTO que usa el ADMIN para actualizar los datos de cualquier usuario.
 //
 // Reglas:
 //  - username, nombreCompleto y email son obligatorios.
  // - password es OPCIONAL:
  //     - Si viene null o vacía → se mantiene la contraseña que ya tenía.
 //     - Si viene con valor → se encripta y se actualiza.

public record ActualizarUsuarioRequest(

        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 150, message = "El nombre completo no puede superar los 150 caracteres")
        String nombreCompleto,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        // Puede venir vacío: el ADMIN no está obligado a cambiar la contraseña
        String password
) {}
