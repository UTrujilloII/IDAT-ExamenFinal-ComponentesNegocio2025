package pe.idat.biblioteca.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.biblioteca.dto.libro.LibroRequest;
import pe.idat.biblioteca.dto.libro.LibroResponse;
import pe.idat.biblioteca.entity.Libro;
import pe.idat.biblioteca.mappers.LibroMapper;
import pe.idat.biblioteca.repository.LibroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService{
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    @Override
    public List<LibroResponse> listarTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();
        return libros.stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void desactivarLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + id + " no existe"));

        if (!libro.isEnabled()) {
            throw new IllegalStateException("El libro ya está desactivado");
        }


        boolean tienePrestamosActivos = libro.getPrestamos().stream()
                .anyMatch(p -> !p.isDevuelto());

        if (tienePrestamosActivos) {
            throw new IllegalStateException("No se puede desactivar el libro porque tiene préstamos activos");
        }

        libro.setEnabled(false);
        libroRepository.save(libro);

    }

    @Override
    public void reactivarLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + id + " no existe"));

        if (libro.isEnabled()) {
            throw new IllegalStateException("El libro ya está activo");
        }

        libro.setEnabled(true);
        libroRepository.save(libro);

    }


    @Override
    public LibroResponse crearLibro(LibroRequest libroRequest) {
        Libro libro = new Libro();
        libro.setTitulo(libroRequest.titulo());
        libro.setAutor(libroRequest.autor());
        libro.setCategoria(libroRequest.categoria());
        libro.setAnioPublicacion(libroRequest.anioPublicacion());
        libro.setStock(libroRequest.stock());
        libro.setEnabled(true);
        return libroMapper.toResponse(libroRepository.save(libro));
    }

    @Override
    public LibroResponse obtenerLibroPorid(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ll libro ingresado no existe"));
        return libroMapper.toResponse(libro);
    }

    @Override
    public LibroResponse actualizarLibro(Long id, LibroRequest libroRequest) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El libro con ID " + id + " no existe"));


        libro.setTitulo(libroRequest.titulo());
        libro.setAutor(libroRequest.autor());
        libro.setCategoria(libroRequest.categoria());
        libro.setAnioPublicacion(libroRequest.anioPublicacion());
        libro.setStock(libroRequest.stock());

        Libro libroActualizado = libroRepository.save(libro);
        return libroMapper.toResponse(libroActualizado);
    }
}
