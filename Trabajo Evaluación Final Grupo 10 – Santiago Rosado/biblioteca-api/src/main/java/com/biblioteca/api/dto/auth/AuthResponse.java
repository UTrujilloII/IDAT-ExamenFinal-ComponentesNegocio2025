package com.biblioteca.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para devolver el token JWT después del login.
 */
@Getter
@Setter
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
