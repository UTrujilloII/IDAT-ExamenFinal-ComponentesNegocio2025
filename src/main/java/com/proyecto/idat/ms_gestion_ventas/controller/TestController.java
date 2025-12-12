package com.proyecto.idat.ms_gestion_ventas.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/me")
    public String me(Authentication auth) {
        return auth.getName() + " -> " + auth.getAuthorities();
    }
}
