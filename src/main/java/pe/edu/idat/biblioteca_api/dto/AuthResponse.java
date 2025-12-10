package pe.edu.idat.biblioteca_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AuthResponse {
    private String token;
}