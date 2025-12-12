package com.biblioteca.biblioteca_api.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String nombre;
    private List<String> roles;
}
