package com.proyecto.idat.ms_gestion_ventas.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//  DTO que usa el ADMIN para actualizar cualquier usuario.
//  No necesita la contraseña actual del usuario.

public record ActualizarUsuarioAdminRequest(

        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede superar los 50 caracteres")
        String username,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 150, message = "El nombre completo no puede superar los 150 caracteres")
        String nombreCompleto,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        // Si viene null o vacío, el admin no cambia la contraseña
        String password
) {}
