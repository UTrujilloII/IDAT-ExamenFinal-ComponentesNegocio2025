package com.biblioteca.api.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para recibir credenciales de login.
 */
@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
}
