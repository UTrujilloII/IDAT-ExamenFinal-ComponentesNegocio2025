package pe.edu.idat.ms_biblioteca.service;

import org.springframework.stereotype.Service;
import pe.edu.idat.ms_biblioteca.dto.LibroDTO;
import pe.edu.idat.ms_biblioteca.entity.Libro;
import pe.edu.idat.ms_biblioteca.repository.LibroRepository;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public LibroDTO crearLibro(LibroDTO dto) {
        Libro libro = new Libro();
        libro.setTitulo(dto.titulo());
        libro.setAutor(dto.autor());
        libro.setCategoria(dto.categoria());
        libro.setCodigo(dto.codigo());
        libro.setStockTotal(dto.stockTotal());
        libro.setStockDisponible(dto.stockDisponible());

        libroRepository.save(libro);

        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCategoria(),
                libro.getCodigo(),
                libro.getStockTotal(),
                libro.getStockDisponible()
        );
    }

    public List<Libro> listar() {
        return libroRepository.findAll();
    }
}
