package com.proyecto.idat.ms_gestion_ventas.security;

import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no viene cabecera Authorization o no empieza con "Bearer ",
        // dejamos pasar la petición tal cual y que otros filtros (o controladores
        // públicos) la gestionen.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el token quitando el prefijo "Bearer "
        String token = authHeader.substring(7);

        try {
            // Aquí puede lanzar ReglaNegocioException (token expirado, inválido, bloqueado, etc.)
            String username = jwtUtils.getUsernameFromToken(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargamos al usuario desde base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validamos nuevamente el token antes de autenticar
                if (jwtUtils.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Marcamos al usuario como autenticado en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Si odo fue bien con el token, continuamos con el resto de filtros / controlador
            filterChain.doFilter(request, response);

        } catch (ReglaNegocioException ex) {

            // ------------------------------------------------------------------
            // AQUÍ DECIDIMOS QUÉ MENSAJE MOSTRAR SEGÚN EL ENDPOINT:
            //
            // - Endpoints solo ADMIN
            //      siempre se dfevuelve  un mensaje genérico de "no tienes permisos",
            //       aunque el token esté expirado o la cuenta bloqueada por token.
            //
            // - Resto de endpoints protegidos
            //      se dev el mensaje DETALLADO que viene desde JwtUtils
            //       (token expirado, intento ... de 3, cuenta bloqueada, etc.).
            //
            // De esta forma
            //    El ADMIN puede ver la info de seguridad en pruebas.
            //    Los usuarios normales no reciben detalles sensibles cuando
            //     intentan acceder a áreas de administración.
            // ------------------------------------------------------------------

            String uri = request.getRequestURI();
            boolean endpointSoloAdmin = uri.startsWith("/api/usuarios");

            String mensaje;

            if (endpointSoloAdmin) {
                // Respuesta para quien intente entrar a endpoints
                // administrativos (tenga o no bloqueo por token).
                mensaje = "No tienes permisos para acceder a este recurso o tu cuenta se encuentra bloqueada. " +
                        "Si crees que es un error, contacta con un administrador.";
            } else {
                //  - "El token ha expirado. Intento 2 de 3."
                //  - "La cuenta del usuario 'nuevoUser42' ha sido bloqueada..."
                mensaje = (ex.getMessage() != null)
                        ? ex.getMessage()
                        : "Token inválido o no autorizado.";
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            String json = """
                    {
                      "timestamp": "%s",
                      "status": 401,
                      "error": "Acceso no autorizado",
                      "message": "%s",
                      "errors": null
                    }
                    """.formatted(
                    Instant.now().toString(),
                    mensaje.replace("\"", "'")
            );

            response.getWriter().write(json);

        }
    }
}
