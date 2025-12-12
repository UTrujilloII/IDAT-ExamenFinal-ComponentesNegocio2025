package pe.edu.idat.biblioteca.security; // ¡AQUÍ ESTÁ LA CLAVE!

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
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. Establecer el estado HTTP a 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 2. Establecer el tipo de contenido como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 3. Escribir el mensaje JSON personalizado
        String jsonResponse = "{\"status\": 403, \"error\": \"Operación Denegada\", \"mensaje\": \"Usted no tiene los permisos para realizar esa acción\"}";

        response.getWriter().write(jsonResponse);
    }
}