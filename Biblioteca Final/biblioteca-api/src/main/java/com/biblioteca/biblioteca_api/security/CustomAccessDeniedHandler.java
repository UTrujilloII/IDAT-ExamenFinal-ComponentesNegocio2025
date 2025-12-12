package com.biblioteca.biblioteca_api.security;

import com.biblioteca.biblioteca_api.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        // Limpia headers + buffer, garantiza JSON limpio
        if (response.isCommitted()) return;
        response.reset();

        ApiError error = ApiError.builder()
                .status(403)
                .error("Acceso denegado")
                .message("No cuenta con permisos para realizar esta acción")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), error);
        response.flushBuffer();
    }
}
