package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.dto.libro.LibroPatchRequest;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.EstadoPrestamo;
import pe.edu.idat.biblioteca.mappers.LibroMapper;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;
import pe.edu.idat.biblioteca.service.impl.LibroService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final PrestamoRepository prestamoRepository;
    private Libro buscarLibroPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Libro no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public LibroResponse registrarLibro(LibroRequest request) {
        if (libroRepository.existsByIsbn(request.isbn())) {
            throw new RuntimeException("El ISBN " + request.isbn() + " ya está registrado en la biblioteca.");
        }
        Libro nuevoLibro = libroMapper.toEntity(request);
        Libro libroGuardado = libroRepository.save(nuevoLibro);
        return libroMapper.toResponse(libroGuardado);
    }

    @Override
    public List<LibroResponse> listarTodos() {
        return libroRepository.findAll().stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LibroResponse obtenerLibro(Long id) {
        Libro libro = buscarLibroPorId(id);
        return libroMapper.toResponse(libro);
    }

    @Override
    @Transactional
    public LibroResponse actualizarLibro(Long id, LibroRequest request) {
        Libro libroExistente = buscarLibroPorId(id);
        if (!libroExistente.getIsbn().equals(request.isbn()) &&
                libroRepository.existsByIsbnAndIdIsNot(request.isbn(), id)) {
            throw new RuntimeException("El ISBN " + request.isbn() + " ya está registrado en otro libro.");
        }
        Libro libroActualizado = libroMapper.updateEntity(request, libroExistente);
        return libroMapper.toResponse(libroRepository.save(libroActualizado));
    }
    @Override
    @Transactional
    public LibroResponse actualizarParcialLibro(Long id, LibroPatchRequest request) {

        Libro existingLibro = buscarLibroPorId(id);
        if (request.isbn() != null && !request.isbn().isEmpty() &&
                !Objects.equals(existingLibro.getIsbn(), request.isbn())) {
            if (libroRepository.existsByIsbnAndIdIsNot(request.isbn(), id)) {
                throw new RuntimeException("El ISBN ya está registrado para otro libro.");
            }
            existingLibro.setIsbn(request.isbn());
        }
        if (request.titulo() != null && !request.titulo().isEmpty()) {
            existingLibro.setTitulo(request.titulo());
        }
        if (request.autor() != null && !request.autor().isEmpty()) {
            existingLibro.setAutor(request.autor());
        }
        if (request.editorial() != null && !request.editorial().isEmpty()) {
            existingLibro.setEditorial(request.editorial());
        }
        if (request.anioPublicacion() != null) {
            existingLibro.setAnioPublicacion(request.anioPublicacion());
        }
        if (request.cantidad() != null) {
            existingLibro.setCantidad(request.cantidad());
        }

        Libro saved = libroRepository.save(existingLibro);
        return libroMapper.toResponse(saved);
    }
    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        Libro libroExistente = buscarLibroPorId(id);
        boolean tienePrestamosActivos = prestamoRepository.existsByLibroIdAndEstado(id, EstadoPrestamo.ACTIVO);
        boolean tienePrestamosVencidos = prestamoRepository.existsByLibroIdAndEstado(id, EstadoPrestamo.VENCIDO);

        if (tienePrestamosActivos || tienePrestamosVencidos) {
            throw new RuntimeException("El libro seleccionado no se puede eliminar porque tiene préstamos pendientes (ACTIVO o VENCIDO). Primero deben devolverse todas las copias.");
        }

        libroRepository.delete(libroExistente);
    }

    @Override
    public List<LibroResponse> listarDisponibles() {
        return libroRepository.findAllDisponibles().stream()
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }
}