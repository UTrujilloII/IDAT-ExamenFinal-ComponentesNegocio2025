package com.proyecto.idat.ms_gestion_ventas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtils {

    private final Key key;
    private final long jwtExpirationMs;

    // Máximo de intentos fallidos de token por usuario
    private static final int MAX_FAILED_ATTEMPTS = 3;

    // Tiempo de bloqueo: 30 minutos en milisegundos
    private static final long LOCK_TIME_MS = 30 * 60 * 1000L;

    // Usuario ADMIN: no se quiere bloquear por token
    private static final String ADMIN_USERNAME = "admin";

    //  cuando el token está tan dañado
    // que no se puede leer el "sub" (username real)
    private static final String USUARIO_DESCONOCIDO = "usuario_desconocido_token";


    // Esta clase interna guarda el estado del bloqueo por token de UN usuario:
    // - intentosFallidos: cuántos tokens inválidos ha enviado
    // - inicioBloqueoMs: cuándo empezó el bloqueo (null si no está bloqueado)

    private static class TokenLockInfo {
        int intentosFallidos;
        Long inicioBloqueoMs;
    }


    // Mapa en memoria:
    //  username -> información del bloqueo por token (intentos + tiempo de inicio)



    private final Map<String, TokenLockInfo> bloqueosTokenPorUsuario = new ConcurrentHashMap<>();

    // ObjectMapper solo para leer el "sub" del payload cuando el token está dañado
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") long jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // Intenta extraer el username (subject) del token sin disparar bloqueo.

    // Casos:
    //  - Token válido → se lee normal.
    //  - Token expirado → se usa las claims del ExpiredJwtException.
    //  - Token con firma inválida / mal formado → plan B:
    //      decodificar el payload y leer el "sub" sin validar la firma.
    //  - Si nada funciona → devuelve null.

    private String extractUsernameSilently(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        try {
            // Token válido (firma correcta y no expirado)
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException ex) {
            // Token expirado pero con firma válida:
            // todavía podemos leer el subject (username)
            return ex.getClaims().getSubject();

        } catch (JwtException | IllegalArgumentException ex) {
            // Firma inválida, token mal formado, etc.
            // Plan B: decodificar el payload para intentar leer "sub"
            return extraerSubjectSinValidarFirma(token);
        }
    }


    private String extraerSubjectSinValidarFirma(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }

            String payload = parts[1];
            byte[] decoded = Base64.getUrlDecoder().decode(payload);
            String json = new String(decoded, StandardCharsets.UTF_8);

            @SuppressWarnings("unchecked")
            Map<String, Object> claims = objectMapper.readValue(json, Map.class);

            Object sub = claims.get("sub");
            return (sub != null) ? sub.toString() : null;

        } catch (Exception e) {
            // Si el token está muy corrupto o no es un JWT válido,
            // simplemente devolvemos null.
            return null;
        }
    }


    private void checkTokenLock(String username) {
        if (username == null
                || ADMIN_USERNAME.equalsIgnoreCase(username)
                || USUARIO_DESCONOCIDO.equals(username)) {
            // No aplicamos bloqueo por token a admin, usuarios desconocidos
            // ni al alias genérico cuando el token está muy dañado.
            return;
        }

        TokenLockInfo info = bloqueosTokenPorUsuario.get(username);
        if (info == null || info.inicioBloqueoMs == null) {
            return; // no hay bloqueo activo para este usuario
        }

        long now = System.currentTimeMillis();
        long elapsed = now - info.inicioBloqueoMs;

        if (elapsed < LOCK_TIME_MS) {
            long remainingMinutes = (LOCK_TIME_MS - elapsed) / 60000;
            throw new ReglaNegocioException(
                    "La cuenta del usuario '" + username + "' está bloqueada por múltiples intentos con token inválido o expirado. " +
                            "Vuelve a intentarlo en aproximadamente " + remainingMinutes + " minutos."
            );
        } else {
            // Ya pasó el tiempo de bloqueo, limpiamos el estado
            info.intentosFallidos = 0;
            info.inicioBloqueoMs = null;
        }
    }


    private int registrarIntentoTokenInvalido(String token, String motivo) {
        // Intentamos obtener el username desde el token (válido, expirado, etc.)
        String username = extractUsernameSilently(token);

        // Si no pudimos extraer el username (token muy dañado),
        // usamos un alias genérico para poder mostrar "Intento X de 3".
        if (username == null) {
            username = USUARIO_DESCONOCIDO;
        }

        // Creamos o recuperamos la info de bloqueo para ese "usuario"
        TokenLockInfo info = bloqueosTokenPorUsuario
                .computeIfAbsent(username, u -> new TokenLockInfo());

        info.intentosFallidos++;

        // Solo bloqueamos CUENTAS REALES:
        // - no bloqueamos al ADMIN
        // - no bloqueamos al alias genérico de usuario desconocido
        boolean seDebeBloquear =
                !ADMIN_USERNAME.equalsIgnoreCase(username)
                        && !USUARIO_DESCONOCIDO.equals(username)
                        && info.intentosFallidos >= MAX_FAILED_ATTEMPTS;

        if (seDebeBloquear) {
            info.inicioBloqueoMs = System.currentTimeMillis();

            throw new ReglaNegocioException(
                    "La cuenta del usuario '" + username + "' ha sido bloqueada por múltiples intentos con token "
                            + motivo + ". Debes esperar 30 minutos antes de volver a usar un token."
            );
        }

        // Devolvemos SIEMPRE el número de intentos acumulados
        // para mostrar: "Intento X de 3" en el mensaje de error.
        return info.intentosFallidos;
    }


    private void limpiarIntentosToken(String username) {
        if (username == null) {
            return;
        }
        TokenLockInfo info = bloqueosTokenPorUsuario.get(username);
        if (info != null) {
            info.intentosFallidos = 0;
            info.inicioBloqueoMs = null;
        }
    }

    private Claims parseClaims(String token) {
        if (token == null || token.isBlank()) {
            // No hay forma de asociarlo a un usuario concreto
            throw new ReglaNegocioException("El token está vacío o no fue enviado.");
        }

        // Intentamos identificar al usuario ANTES de validar completamente el token
        String username = extractUsernameSilently(token);

        // Primero, verificamos si ese usuario ya está bloqueado por token
        if (username != null) {
            checkTokenLock(username);
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Si el token es válido, limpiamos el contador de ese usuario
            if (username == null) {
                username = claims.getSubject();
            }
            limpiarIntentosToken(username);

            return claims;

        } catch (ExpiredJwtException ex) {
            int intentos = registrarIntentoTokenInvalido(token, "expirado");
            throw new ReglaNegocioException(
                    "El token ha expirado. Vuelve a iniciar sesión. " +
                            (intentos > 0
                                    ? "Intento " + intentos + " de " + MAX_FAILED_ATTEMPTS + "."
                                    : "")
            );

        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            int intentos = registrarIntentoTokenInvalido(token, "inválido o corrupto");
            throw new ReglaNegocioException(
                    "El token es inválido o está corrupto. " +
                            (intentos > 0
                                    ? "Intento " + intentos + " de " + MAX_FAILED_ATTEMPTS + "."
                                    : "")
            );

        } catch (io.jsonwebtoken.security.SignatureException ex) {
            int intentos = registrarIntentoTokenInvalido(token, "con firma inválida");
            throw new ReglaNegocioException(
                    "La firma del token no es válida. " +
                            (intentos > 0
                                    ? "Intento " + intentos + " de " + MAX_FAILED_ATTEMPTS + "."
                                    : "")
            );

        } catch (IllegalArgumentException ex) {
            int intentos = registrarIntentoTokenInvalido(token, "vacío o mal enviado");
            throw new ReglaNegocioException(
                    "El token está vacío o no fue enviado correctamente. " +
                            (intentos > 0
                                    ? "Intento " + intentos + " de " + MAX_FAILED_ATTEMPTS + "."
                                    : "")
            );
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        parseClaims(token); // si es inválido, aquí lanza excepción con el motivo
        return true;
    }

    // ==========================
    //   Métodos públicos para ADMIN / controlador
    // ==========================


    public Map<String, Long> obtenerUsuariosBloqueadosPorToken() {
        Map<String, Long> resultado = new HashMap<>();
        long now = System.currentTimeMillis();

        for (Map.Entry<String, TokenLockInfo> entry : bloqueosTokenPorUsuario.entrySet()) {
            String username = entry.getKey();
            TokenLockInfo info = entry.getValue();

            if (info.inicioBloqueoMs == null) {
                continue;
            }

            long elapsed = now - info.inicioBloqueoMs;
            long remainingMs = LOCK_TIME_MS - elapsed;

            if (remainingMs > 0) {
                long remainingMinutes = remainingMs / 60000;
                resultado.put(username, remainingMinutes);
            } else {
                // El bloqueo ya venció, limpiamos
                info.intentosFallidos = 0;
                info.inicioBloqueoMs = null;
            }
        }

        return resultado;
    }


    public void desbloquearBloqueoTokenUsuario(String username) {
        if (username == null) {
            return;
        }
        TokenLockInfo info = bloqueosTokenPorUsuario.get(username);
        if (info != null) {
            info.intentosFallidos = 0;
            info.inicioBloqueoMs = null;
        }
    }


    public boolean estaBloqueadoPorToken(String username) {
        if (username == null) {
            return false;
        }
        TokenLockInfo info = bloqueosTokenPorUsuario.get(username);
        if (info == null || info.inicioBloqueoMs == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - info.inicioBloqueoMs;
        return elapsed < LOCK_TIME_MS;
    }
}
