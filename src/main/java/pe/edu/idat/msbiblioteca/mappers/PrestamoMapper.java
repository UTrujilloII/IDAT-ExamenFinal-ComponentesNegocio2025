package pe.edu.idat.msbiblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pe.edu.idat.msbiblioteca.dto.prestamo.PrestamoResponseDTO;
import pe.edu.idat.msbiblioteca.entity.Prestamo;

/**
 * Mapper para convertir entre entidades Prestamo y sus DTOs.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrestamoMapper {

    /**
     * Convierte una entidad Prestamo a un PrestamoResponseDTO.
     * Mapea los datos del usuario y libro asociados.
     *
     * @param entity Entidad Prestamo
     * @return DTO de respuesta con datos del préstamo
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.username", target = "usuarioNombre")
    @Mapping(source = "libro.id", target = "libroId")
    @Mapping(source = "libro.titulo", target = "libroTitulo")
    @Mapping(source = "libro.autor", target = "libroAutor")
    @Mapping(source = "libro.isbn", target = "libroIsbn")
    @Mapping(target = "diasRetraso", expression = "java(entity.calcularDiasRetraso())")
    PrestamoResponseDTO toResponseDTO(Prestamo entity);
}
