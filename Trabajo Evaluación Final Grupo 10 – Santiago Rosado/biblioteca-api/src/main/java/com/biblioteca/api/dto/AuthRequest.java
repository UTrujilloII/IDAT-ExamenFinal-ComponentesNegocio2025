package com.biblioteca.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para recibir username y password
 * en los endpoints /login y /register
 */
@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
}
