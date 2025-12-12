package com.biblioteca.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * =============================================
 * CONTROLADOR DE PRUEBA
 * Permite verificar que la aplicación arranca,
 * responde peticiones HTTP y está correctamente
 * configurada antes de iniciar el desarrollo real.
 * =============================================
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "API Biblioteca funcionando correctamente ✔️";
    }
}