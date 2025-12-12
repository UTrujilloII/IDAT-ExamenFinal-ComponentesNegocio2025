package com.biblioteca.api.exception;

/**
 * =========================================================
 * EXCEPCIÓN PERSONALIZADA PARA RECURSOS NO ENCONTRADOS
 * ---------------------------------------------------------
 * Esta excepción será lanzada cuando se intente buscar un
 * recurso (ejemplo: libro) que NO exista en la base de datos.
 * =========================================================
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
