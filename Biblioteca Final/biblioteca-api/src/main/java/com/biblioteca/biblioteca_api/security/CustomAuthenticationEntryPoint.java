package com.biblioteca.biblioteca_api.security;

import com.biblioteca.biblioteca_api.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        if (response.isCommitted()) return;
        response.reset();

        ApiError error = ApiError.builder()
                .status(401)
                .error("No autorizado")
                .message("Debe iniciar sesión o proporcionar un token válido")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), error);
        response.flushBuffer();
    }
}
