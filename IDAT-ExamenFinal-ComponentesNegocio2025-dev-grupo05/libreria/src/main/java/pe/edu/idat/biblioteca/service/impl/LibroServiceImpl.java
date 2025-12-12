package pe.edu.idat.biblioteca.service.impl;


import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.mappers.LibroMapper;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.PrestamoRepository;

import java.util.List;
import java.util.NoSuchElementException;
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
        Libro nuevoLibro = libroMapper.toEntity(request);
        nuevoLibro.setCantidadDisponible(nuevoLibro.getCantidadTotal());
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
        // La variable 'id' se resuelve del encabezado del método
        Libro libroExistente = buscarLibroPorId(id);

        // 1. Obtener los valores de stock actuales y nuevos
        Integer oldTotal = libroExistente.getCantidadTotal();
        Integer newTotal = request.cantidadTotal();
        int diff = newTotal - oldTotal;

        // 2. Comprobar si la reducción de stock es válida
        if (newTotal < (oldTotal - libroExistente.getCantidadDisponible())) {
            throw new RuntimeException("No se puede reducir la cantidad total de libros. Hay " + (oldTotal - libroExistente.getCantidadDisponible()) + " copias prestadas actualmente.");
        }

        // 3. Actualizar campos escalares (usando el Request)
        libroExistente.setTitulo(request.titulo());
        libroExistente.setAutor(request.autor());
        libroExistente.setEditorial(request.editorial());
        libroExistente.setCategoria(request.categoria());

        // 4. Actualizar cantidades
        libroExistente.setCantidadTotal(newTotal);
        libroExistente.setCantidadDisponible(libroExistente.getCantidadDisponible() + diff);

        return libroMapper.toResponse(libroRepository.save(libroExistente));
    }

    @Override
    @Transactional
    public LibroResponse actualizarParcialLibro(Long id, Object request) {
        Libro existingLibro = buscarLibroPorId(id);
        // Implementación de PATCH omitida para simplificar y enfocarnos en la compilación
        return libroMapper.toResponse(libroRepository.save(existingLibro));
    }

    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        Libro libroExistente = buscarLibroPorId(id);


        boolean tienePrestamosPendientes = prestamoRepository.existsByLibroIdAndEstado(id, Prestamo.EstadoPrestamo.ACTIVO) ||
                prestamoRepository.existsByLibroIdAndEstado(id, Prestamo.EstadoPrestamo.VENCIDO);

        if (tienePrestamosPendientes) {
            throw new RuntimeException("El libro tiene un prestamo activo, no puede ser eliminado");
        }

        libroRepository.delete(libroExistente);
    }

    @Override
    public List<LibroResponse> listarDisponibles() {
        return libroRepository.findByActivoTrue().stream()
                .filter(libro -> libro.getCantidadDisponible() > 0)
                .map(libroMapper::toResponse)
                .collect(Collectors.toList());
    }
}