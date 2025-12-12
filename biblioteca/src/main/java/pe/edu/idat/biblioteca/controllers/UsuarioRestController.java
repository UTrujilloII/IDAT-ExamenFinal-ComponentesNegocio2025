package pe.edu.idat.biblioteca.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.idat.biblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.service.UsuarioService;

import java.util.List;


@RestController
@RequestMapping("/v1/usuario")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Crear usuario ,es publico")
    @PostMapping
    public ResponseEntity<JwtResponse> createAccount(@Valid @RequestBody UsuarioRequest usuarioRequest){
        return new ResponseEntity<>(usuarioService.createAccount(usuarioRequest), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Buscar usuario por id  para rol \"ADMIN\" y \"USER\"")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(usuarioService.findById(id),HttpStatus.OK);
    }

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Lista de usuario para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios(){
        return new ResponseEntity<>(usuarioService.listarUsuarios(),HttpStatus.OK);
    }

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Actualizar usuario por id  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> updateUsuario(@PathVariable Long id,@Valid @RequestBody UsuarioRequest usuarioRequest){
        return new ResponseEntity<>(usuarioService.updateUsuario(id,usuarioRequest),HttpStatus.OK);
    }

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "eliminar usuario por id para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id){
        usuarioService.deleteUsuario(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
