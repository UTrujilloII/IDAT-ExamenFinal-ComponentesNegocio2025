package com.biblioteca.biblioteca_api.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Correo inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Nuevo campo opcional: roles
    private Set<String> roles;
}

