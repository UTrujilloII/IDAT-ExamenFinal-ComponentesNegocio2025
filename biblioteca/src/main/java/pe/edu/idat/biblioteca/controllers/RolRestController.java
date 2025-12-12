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
import pe.edu.idat.biblioteca.dto.rol.RolRequest;
import pe.edu.idat.biblioteca.dto.rol.RolResponse;
import pe.edu.idat.biblioteca.service.RolService;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/v1/rol")
@RequiredArgsConstructor
public class RolRestController {

    private final RolService rolService;

    @Operation(summary = "Crear rol  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RolResponse> createRol(@Valid @RequestBody RolRequest rolRequest){
        return new ResponseEntity<>(rolService.createRol(rolRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar roles  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RolResponse>> listarRoles(){
        return new ResponseEntity<>(rolService.listarRoles(),HttpStatus.OK);
    }

    @Operation(summary = "buscar rol por id  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(rolService.findById(id),HttpStatus.OK);
    }

    @Operation(summary = "Actualizar rol por id  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse>updateRol(@PathVariable Long id ,@Valid @RequestBody RolRequest rolRequest){
        return new ResponseEntity<>(rolService.updateRol(id,rolRequest),HttpStatus.OK);
    }

    @Operation(summary = "Eliminar rol por id  para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteRol(@PathVariable Long id){
        rolService.deleteRol(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
