package com.proyecto.idat.ms_gestion_ventas.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//  DTO que usa el propio usuario para actualizar su cuenta.
//  Solo puede modificar SUS datos, nunca los de otro usuario.

//  passwordActual:
//    - Se usa para verificar su identidad antes de cambiar nada.

//  nuevaPassword:
//    - Si viene null o vacía, se mantiene la contraseña anterior.
//    - Si trae un valor, se actualiza la contraseña.

public record ActualizarMiCuentaRequest(

        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede superar los 50 caracteres")
        String username,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 150, message = "El nombre completo no puede superar los 150 caracteres")
        String nombreCompleto,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "La contraseña actual es obligatoria")
        String passwordActual,

        // Opcional: si no viene, se mantiene la anterior
        String nuevaPassword
) {}
