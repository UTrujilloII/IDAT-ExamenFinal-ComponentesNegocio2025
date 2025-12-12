package com.proyecto.idat.ms_gestion_ventas.service.impl;

import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.libro.LibroResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Categoria;
import com.proyecto.idat.ms_gestion_ventas.entity.Libro;
import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import com.proyecto.idat.ms_gestion_ventas.exception.RecursoNoEncontradoException;
import com.proyecto.idat.ms_gestion_ventas.mappers.LibroMapper;
import com.proyecto.idat.ms_gestion_ventas.repository.CategoryRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.LibroRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.PrestamoRepository;
import com.proyecto.idat.ms_gestion_ventas.service.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;
    private final CategoryRepository categoriaRepository;
    private final LibroMapper libroMapper;

    @Override
    public LibroResponse crear(LibroRequest request) {

        // Validar ISBN duplicado
        if (libroRepository.existsByIsbn(request.isbn())) {
            throw new ReglaNegocioException(
                    "Ya existe un libro registrado con el ISBN '" + request.isbn() + "'"
            );
        }

        // Buscar categoría
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con id " + request.categoriaId()
                ));

        // Usamos mapper para pasar de DTO → Entidad
        Libro libro = libroMapper.toEntity(request);

        // Setear categoría y ejemplaresDisponibles
        libro.setCategoria(categoria);
        libro.setEjemplaresDisponibles(request.ejemplaresTotales());

        Libro guardado = libroRepository.save(libro);

        // Mapper Entidad → DTO
        return libroMapper.toResponse(guardado);
    }

    @Override
    public List<LibroResponse> listar() {
        return libroRepository.findAll().stream()
                .map(libroMapper::toResponse)
                .toList();
    }

    @Override
    public LibroResponse obtenerPorId(Long idLibro) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + idLibro
                ));
        return libroMapper.toResponse(libro);
    }

    @Override
    public void eliminar(Long idLibro) {
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + idLibro
                ));

        if (prestamoRepository.existsByLibro(libro)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el libro '" + libro.getTitulo() +
                            "' porque tiene préstamos asociados."
            );
        }
        libroRepository.delete(libro);
    }

    @Override
    public LibroResponse actualizar(Long idLibro, LibroRequest request) {

        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el libro con id " + idLibro
                ));

        // 1) Validar cambio de ISBN (si se cambió)
        if (!libro.getIsbn().equals(request.isbn())
                && libroRepository.existsByIsbn(request.isbn())) {
            throw new ReglaNegocioException(
                    "Ya existe un libro registrado con el ISBN '" + request.isbn() + "'"
            );
        }

        // 2) Calcular préstamos activos antes de actualizar
        Integer totAntes = libro.getEjemplaresTotales();
        Integer dispAntes = libro.getEjemplaresDisponibles();
        int prestamosActivos = totAntes - dispAntes;

        // 3) Actualizar campos básicos con el mapper
        libroMapper.updateFromRequest(request, libro);

        //  3.1) Actualizar categoría (obligatorias)
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con id " + request.categoriaId()
                ));
        libro.setCategoria(categoria);

        // 4) Recalcular disponibles según nuevo total
        Integer nuevoTotal = request.ejemplaresTotales();

        if (nuevoTotal < prestamosActivos) {
            throw new ReglaNegocioException(
                    "No se puede establecer ejemplares totales en " + nuevoTotal +
                            " porque hay " + prestamosActivos + " préstamos activos."
            );
        }

        int nuevaDisponibilidad = nuevoTotal - prestamosActivos;
        libro.setEjemplaresDisponibles(nuevaDisponibilidad);

        Libro guardado = libroRepository.save(libro);
        return libroMapper.toResponse(guardado);
    }
}
