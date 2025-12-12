package pe.edu.idat.msbiblioteca.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.idat.msbiblioteca.security.UserDetailServiceImpl;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter
{
    private final JwtUtil jwtUtil;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.startsWith("/v1/auth/");

        if (shouldNotFilter) {
            log.debug("Saltando filtro JWT para path: {}", path);
        }

        return shouldNotFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        String username = null;
        String token = null;

        log.debug("Procesando petición: {} {}", request.getMethod(), requestURI);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No se encontró header Authorization o no tiene formato Bearer para: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(7);
            log.debug("Token extraído: {}...", token.substring(0, Math.min(20, token.length())));

            if(jwtUtil.validateToken(token)) {
                username = jwtUtil.extractUsername(token);
                log.debug("Token válido para usuario: {}", username);
            } else {
                log.warn("Token inválido o expirado");
            }

        } catch (Exception e) {
            log.error("Error al procesar token JWT: {}", e.getMessage());
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                log.debug("Usuario cargado: {} con authorities: {}", username, userDetails.getAuthorities());

                if(jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("Autenticación exitosa para usuario: {} con roles: {}",
                            username, userDetails.getAuthorities());
                } else {
                    log.warn("Token no válido en segunda validación");
                }
            } catch (Exception e) {
                log.error("Error al cargar usuario o crear autenticación: {}", e.getMessage());
            }
        } else if (username == null) {
            log.warn("No se pudo extraer username del token");
        }

        filterChain.doFilter(request, response);
    }
}