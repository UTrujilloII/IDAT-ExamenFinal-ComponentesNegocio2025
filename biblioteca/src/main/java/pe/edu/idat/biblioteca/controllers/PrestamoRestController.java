package pe.edu.idat.biblioteca.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.service.PrestamosService;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/v1/prestamo")
@RequiredArgsConstructor
public class PrestamoRestController {

    private final PrestamosService prestamosService;

    @Operation(summary = "Crear prestamo para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PrestamoResponse> createPrestamo(@Valid @RequestBody PrestamoRequest prestamoRequest){
        return new ResponseEntity<>(prestamosService.createPrestamo(prestamoRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar prestamo por id para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponse>findById(@PathVariable Long id){
        return new ResponseEntity<>(prestamosService.findById(id),HttpStatus.OK);
    }

    @Operation(summary = "Listar prestamos para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<PrestamoResponse>>listarPrestamo(){
        return new ResponseEntity<>(prestamosService.listarPrestamo(),HttpStatus.OK);
    }

    @Operation(summary = "listar prestamo por id de usuario para rol \"ADMIN\" y \"USER\"")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PrestamoResponse>>listarPrestamoPorId(@PathVariable Long idUsuario){
        return new ResponseEntity<>(prestamosService.listarPrestamoPorId(idUsuario),HttpStatus.OK);
    }


    @Operation(summary = "devolucion por id para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/devolucion/{id}")
    public ResponseEntity<Void>devolverPrestamo(@PathVariable Long id){
        prestamosService.devolverPrestamo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
