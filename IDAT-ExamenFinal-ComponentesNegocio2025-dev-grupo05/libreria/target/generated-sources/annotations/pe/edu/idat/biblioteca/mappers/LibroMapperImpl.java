package pe.edu.idat.biblioteca.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T08:58:27-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class LibroMapperImpl implements LibroMapper {

    @Override
    public Libro toEntity(LibroRequest request) {
        if ( request == null ) {
            return null;
        }

        Libro libro = new Libro();

        libro.setCantidadDisponible( request.cantidadTotal() );
        libro.setTitulo( request.titulo() );
        libro.setAutor( request.autor() );
        libro.setEditorial( request.editorial() );
        libro.setCategoria( request.categoria() );
        libro.setCantidadTotal( request.cantidadTotal() );

        return libro;
    }

    @Override
    public LibroResponse toResponse(Libro libro) {
        if ( libro == null ) {
            return null;
        }

        Long id = null;
        String titulo = null;
        String autor = null;
        String editorial = null;
        String categoria = null;
        Integer cantidadTotal = null;
        Integer cantidadDisponible = null;
        Boolean activo = null;

        id = libro.getId();
        titulo = libro.getTitulo();
        autor = libro.getAutor();
        editorial = libro.getEditorial();
        categoria = libro.getCategoria();
        cantidadTotal = libro.getCantidadTotal();
        cantidadDisponible = libro.getCantidadDisponible();
        activo = libro.getActivo();

        LibroResponse libroResponse = new LibroResponse( id, titulo, autor, editorial, categoria, cantidadTotal, cantidadDisponible, activo );

        return libroResponse;
    }
}
