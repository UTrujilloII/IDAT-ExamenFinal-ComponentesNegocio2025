package pe.edu.idat.biblioteca_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.idat.biblioteca_api.model.Libro;
import pe.edu.idat.biblioteca_api.repository.LibroRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok inyecta automáticamente el repositorio en el constructor
public class LibroService {

    private final LibroRepository libroRepository;

    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }
    
    public Optional<Libro> obtenerPorId(Long id){
        return libroRepository.findById(id);
    }
    
    // Método necesario para disminuir stock al prestar
    // Cumple con "lógica de negocio" separada 
    public boolean hayStock(Long idLibro) {
        Optional<Libro> libro = libroRepository.findById(idLibro);
        return libro.isPresent() && libro.get().getStock() > 0;
    }
}