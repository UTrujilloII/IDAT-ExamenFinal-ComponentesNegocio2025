package pe.edu.idat.biblioteca.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.idat.biblioteca.dto.libro.LibroRequest;
import pe.edu.idat.biblioteca.dto.libro.LibroResponse;
import pe.edu.idat.biblioteca.entity.Libro;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-11T21:45:37-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class LibroMapperImpl implements LibroMapper {

    @Override
    public LibroResponse toResponse(Libro libro) {
        if ( libro == null ) {
            return null;
        }

        Long id = null;
        String titulo = null;
        String autor = null;
        String editorial = null;
        String isbn = null;
        Integer anioPublicacion = null;
        Integer cantidad = null;

        id = libro.getId();
        titulo = libro.getTitulo();
        autor = libro.getAutor();
        editorial = libro.getEditorial();
        isbn = libro.getIsbn();
        anioPublicacion = libro.getAnioPublicacion();
        cantidad = libro.getCantidad();

        String disponible = libro.getCantidad() > 0 ? "Sí" : "No";

        LibroResponse libroResponse = new LibroResponse( id, titulo, autor, editorial, isbn, anioPublicacion, cantidad, disponible );

        return libroResponse;
    }

    @Override
    public Libro toEntity(LibroRequest request) {
        if ( request == null ) {
            return null;
        }

        Libro libro = new Libro();

        libro.setTitulo( request.titulo() );
        libro.setAutor( request.autor() );
        libro.setEditorial( request.editorial() );
        libro.setIsbn( request.isbn() );
        libro.setAnioPublicacion( request.anioPublicacion() );
        libro.setCantidad( request.cantidad() );

        return libro;
    }

    @Override
    public Libro updateEntity(LibroRequest request, Libro libro) {
        if ( request == null ) {
            return libro;
        }

        libro.setTitulo( request.titulo() );
        libro.setAutor( request.autor() );
        libro.setEditorial( request.editorial() );
        libro.setIsbn( request.isbn() );
        libro.setAnioPublicacion( request.anioPublicacion() );
        libro.setCantidad( request.cantidad() );

        return libro;
    }
}
