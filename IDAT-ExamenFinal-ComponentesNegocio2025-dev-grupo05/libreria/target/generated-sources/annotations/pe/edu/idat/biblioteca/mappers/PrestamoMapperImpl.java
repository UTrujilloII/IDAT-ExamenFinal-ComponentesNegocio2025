package pe.edu.idat.biblioteca.mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoRequest;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.entity.Usuario;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-12T08:58:28-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class PrestamoMapperImpl implements PrestamoMapper {

    @Override
    public PrestamoResponse toResponse(Prestamo prestamo) {
        if ( prestamo == null ) {
            return null;
        }

        Long usuarioId = null;
        Long libroId = null;
        String tituloLibro = null;
        Long id = null;
        LocalDateTime fechaPrestamo = null;
        LocalDate fechaDevolucionEsperada = null;
        LocalDateTime fechaDevolucionReal = null;
        String estado = null;

        usuarioId = prestamoUsuarioId( prestamo );
        libroId = prestamoLibroId( prestamo );
        tituloLibro = prestamoLibroTitulo( prestamo );
        id = prestamo.getId();
        fechaPrestamo = prestamo.getFechaPrestamo();
        fechaDevolucionEsperada = prestamo.getFechaDevolucionEsperada();
        fechaDevolucionReal = prestamo.getFechaDevolucionReal();
        if ( prestamo.getEstado() != null ) {
            estado = prestamo.getEstado().name();
        }

        String nombreUsuario = prestamo.getUsuario().getNombre() + " " + prestamo.getUsuario().getApellido();

        PrestamoResponse prestamoResponse = new PrestamoResponse( id, usuarioId, nombreUsuario, libroId, tituloLibro, fechaPrestamo, fechaDevolucionEsperada, fechaDevolucionReal, estado );

        return prestamoResponse;
    }

    @Override
    public Prestamo toEntity(PrestamoRequest request) {
        if ( request == null ) {
            return null;
        }

        Prestamo prestamo = new Prestamo();

        prestamo.setUsuario( prestamoRequestToUsuario( request ) );
        prestamo.setLibro( prestamoRequestToLibro( request ) );
        prestamo.setFechaDevolucionEsperada( request.fechaDevolucionEsperada() );

        return prestamo;
    }

    private Long prestamoUsuarioId(Prestamo prestamo) {
        Usuario usuario = prestamo.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        return usuario.getId();
    }

    private Long prestamoLibroId(Prestamo prestamo) {
        Libro libro = prestamo.getLibro();
        if ( libro == null ) {
            return null;
        }
        return libro.getId();
    }

    private String prestamoLibroTitulo(Prestamo prestamo) {
        Libro libro = prestamo.getLibro();
        if ( libro == null ) {
            return null;
        }
        return libro.getTitulo();
    }

    protected Usuario prestamoRequestToUsuario(PrestamoRequest prestamoRequest) {
        if ( prestamoRequest == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( prestamoRequest.usuarioId() );

        return usuario;
    }

    protected Libro prestamoRequestToLibro(PrestamoRequest prestamoRequest) {
        if ( prestamoRequest == null ) {
            return null;
        }

        Libro libro = new Libro();

        libro.setId( prestamoRequest.libroId() );

        return libro;
    }
}
