package pe.edu.idat.biblioteca_api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403); // Código HTTP Forbidden
        response.setContentType("application/json;charset=UTF-8");
        // Aquí personalizas tu mensaje JSON:
        response.getWriter().write("{\"error\": \"Acceso denegado\", \"mensaje\": \"No es usuario autorizado para realizar esta operación\"}");
    }
}