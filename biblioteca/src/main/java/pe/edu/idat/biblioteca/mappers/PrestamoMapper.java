package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    // --- Mapeo de Entidad a Respuesta (CORREGIDO) ---
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "username", source = "usuario.username")
    @Mapping(target = "libroId", source = "libro.id")
    @Mapping(target = "tituloLibro", source = "libro.titulo")

    // CORRECCIÓN: MapStruct convierte el Enum (EstadoPrestamo) a String (estado) automáticamente.
    // Simplemente mapeamos el campo.
    @Mapping(target = "estado", source = "estado")

    PrestamoResponse toResponse(Prestamo prestamo);

    // El método toEntity fue eliminado, como se discutió, porque la lógica de creación
    // ahora reside en PrestamoServiceImpl, donde se itera sobre List<PrestamoItemRequest>.
}