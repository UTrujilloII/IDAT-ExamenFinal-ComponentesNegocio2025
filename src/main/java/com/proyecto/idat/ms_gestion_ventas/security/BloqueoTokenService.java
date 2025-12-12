package com.proyecto.idat.ms_gestion_ventas.security;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BloqueoTokenService {

    private static final int MAX_INTENTOS = 3;
    private static final long MINUTOS_BLOQUEO = 30;


    private static final String ADMIN_USERNAME = "admin";

    private final Map<String, Integer> intentos = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> bloqueadoHasta = new ConcurrentHashMap<>();


    //   helper para saber si es el admin intocable
    private boolean esAdmin(String username) {
        return ADMIN_USERNAME.equalsIgnoreCase(username);
    }

    public boolean estaBloqueado(String username) {
        //   el admin nunca está bloqueado
        if (esAdmin(username)) {
            return false;
        }

        LocalDateTime hasta = bloqueadoHasta.get(username);
        if (hasta == null) {
            return false;
        }
        if (hasta.isBefore(LocalDateTime.now())) {
            // ya pasó el tiempo, y limpiamos
            bloqueadoHasta.remove(username);
            intentos.remove(username);
            return false;
        }
        return true;
    }



    // minutos que le quedan bloqueado a un usuario (==0 si no hay bloqueo)
    public long obtenerMinutosRestantes(String username) {
        LocalDateTime hasta = bloqueadoHasta.get(username);
        if (hasta == null) {
            return 0;
        }
        long minutos = Duration.between(LocalDateTime.now(), hasta).toMinutes();
        return Math.max(minutos, 0);
    }



    public boolean registrarIntentoFallido(String username) {
        // no contamos intentos para el admin
        if (esAdmin(username)) {
            return false;
        }

        int nuevo = intentos.getOrDefault(username, 0) + 1;
        intentos.put(username, nuevo);

        if (nuevo >= MAX_INTENTOS) {
            bloqueadoHasta.put(username, LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
            return true;
        }
        return false;
    }

    public void limpiarIntentos(String username) {
        //  para admin no hace falta
        intentos.remove(username);
        bloqueadoHasta.remove(username);
    }

    //  para que el ADMIN pueda desbloquear manualmente
    public void desbloquear(String username) {
        limpiarIntentos(username);
    }



    // lista de usuarios bloqueados actualmente con minutos restanteSS
    public Map<String, Long> obtenerUsuariosBloqueados() {
        Map<String, Long> resultado = new ConcurrentHashMap<>();
        LocalDateTime ahora = LocalDateTime.now();

        bloqueadoHasta.forEach((username, hasta) -> {
            // se ignora admin por regla de negocio
            if (esAdmin(username)) {
                return;
            }

            if (hasta.isBefore(ahora)) {
                //     si ya vencio el bloqueo, se limpia
                bloqueadoHasta.remove(username);
                intentos.remove(username);
            } else {
                long minutosRestantes = Duration.between(ahora, hasta).toMinutes();
                if (minutosRestantes < 0) {
                    minutosRestantes = 0;
                }
                resultado.put(username, minutosRestantes);
            }
        });

        return resultado;
    }
}
