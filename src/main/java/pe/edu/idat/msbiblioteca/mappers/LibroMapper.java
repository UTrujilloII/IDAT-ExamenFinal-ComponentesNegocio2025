package pe.edu.idat.msbiblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pe.edu.idat.msbiblioteca.dto.libro.LibroRequestDTO;
import pe.edu.idat.msbiblioteca.dto.libro.LibroResponseDTO;
import pe.edu.idat.msbiblioteca.entity.Libro;

/**
 * Mapper para convertir entre la entidad Libro y sus DTOs.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LibroMapper {

    /**
     * Convierte un LibroRequestDTO a una entidad Libro.
     * Establece copias disponibles igual a copias totales si no se especifica.
     *
     * @param dto DTO de solicitud con datos del libro
     * @return Entidad Libro
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "DISPONIBLE")
    @Mapping(target = "fechaRegistro", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "prestamos", ignore = true)
    @Mapping(target = "copiasDisponibles",
             expression = "java(dto.getCopiasDisponibles() != null ? dto.getCopiasDisponibles() : dto.getCopiasTotales())")
    Libro toEntity(LibroRequestDTO dto);

    /**
     * Convierte una entidad Libro a un LibroResponseDTO.
     *
     * @param entity Entidad Libro
     * @return DTO de respuesta con datos del libro
     */
    LibroResponseDTO toResponseDTO(Libro entity);

    /**
     * Actualiza una entidad Libro existente con datos de un LibroRequestDTO.
     * Solo actualiza los campos no nulos del DTO.
     *
     * @param dto DTO con nuevos datos
     * @param entity Entidad existente a actualizar
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "prestamos", ignore = true)
    void updateEntityFromDTO(LibroRequestDTO dto, @MappingTarget Libro entity);
}
