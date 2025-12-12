package pe.edu.idat.ms_biblioteca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pe.edu.idat.ms_biblioteca.dto.LibroDTO;
import pe.edu.idat.ms_biblioteca.service.LibroService;
import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LibroDTO crearLibro(@RequestBody LibroDTO dto) {
        return libroService.crearLibro(dto);
    }

    @GetMapping
    public List<?> listar() {
        return libroService.listar();
    }
}
