package pe.edu.idat.biblioteca.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.idat.biblioteca.dto.auth.LoginRequest;
import pe.edu.idat.biblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.service.AuthService;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "autenticación", description = "Endpoint para loguearse")
public class AuthResController {

    private final AuthService authService;


    @Operation(summary = "loguearse publico para todos")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse>login(@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

//    @Operation(summary = "obtener nuevo token")
//    @@PostMapping("/refresh-token")
//    public ResponseEntity<JwtResponse>refreshToken()
}
