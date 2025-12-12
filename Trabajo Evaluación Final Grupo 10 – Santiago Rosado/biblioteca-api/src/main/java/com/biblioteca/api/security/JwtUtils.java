package com.biblioteca.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ================================================
 * UTILIDAD JWT
 * Genera, firma, valida y extrae datos del token.
 * ================================================
 */
@Component
public class JwtUtils {

    // Clave secreta BASE64 (debes reemplazarla por tu clave generada)
    private static final String SECRET_KEY = "3bk+Ioukeck6tcuhqVc02qPfKAAUPQPpOx2EXzmJmnE=Y";

    // Tiempo de expiración del token → 24 horas
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    // ============================================================
    // OBTENER USERNAME DESDE TOKEN
    // ============================================================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extrae una información específica del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ============================================================
    // GENERAR TOKEN
    // ============================================================
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================================================
    // VALIDAR TOKEN
    // ============================================================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ============================================================
    // OBTENER CLAIMS (Contenido firmado del token)
    // ============================================================
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ============================================================
    // OBTENER LLAVE DE FIRMA DESDE SECRET_KEY BASE64
    // ============================================================
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
