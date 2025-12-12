package com.biblioteca.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para devolver el token JWT al usuario
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
