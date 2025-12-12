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
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.service.LibroService;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/v1/libro")
@RequiredArgsConstructor
public class LibroRestController {
    private final LibroService libroService;

    @Operation(summary = "crear libro solo para rol \"ADMIN\"")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LibroResponse> createLibro(@Valid @RequestBody LibroRequest libroRequest){
        return new ResponseEntity<>(libroService.createLibro(libroRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar libro por id para rol \"ADMIN\" y \"USER\" ")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse>findById(@PathVariable Long id){
        return new ResponseEntity<>(libroService.findById(id),HttpStatus.OK);
    }

    @Operation(summary = "Listar libros para rol \"ADMIN\" y \"USER\" ")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<LibroResponse>>listarLibros(){
        return new ResponseEntity<>(libroService.listarLibros(), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar libro por id para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> updateLibro(@PathVariable Long id, @Valid @RequestBody LibroRequest libroRequest){
        return new ResponseEntity<>(libroService.updateLibro(id,libroRequest),HttpStatus.OK);
    }

    @Operation(summary = "eliminar libro por id para rol \"ADMIN\"")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id){
        libroService.deleteLibro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
