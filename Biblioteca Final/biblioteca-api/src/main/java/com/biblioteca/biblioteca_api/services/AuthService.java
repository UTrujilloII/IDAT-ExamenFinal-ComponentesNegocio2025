package com.biblioteca.biblioteca_api.services;

import com.biblioteca.biblioteca_api.dtos.auth.AuthRequest;
import com.biblioteca.biblioteca_api.dtos.auth.AuthResponse;
import com.biblioteca.biblioteca_api.dtos.auth.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);
}
