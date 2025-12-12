package com.proyecto.idat.ms_gestion_ventas.service;

import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthLoginRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthRegisterRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRegisterRequest request);

    AuthResponse login(AuthLoginRequest request);

    AuthResponse refreshToken(String bearerToken);
}
