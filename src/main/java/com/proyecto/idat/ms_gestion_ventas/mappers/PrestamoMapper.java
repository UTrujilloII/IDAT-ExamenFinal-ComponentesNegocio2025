package com.proyecto.idat.ms_gestion_ventas.mappers;

import com.proyecto.idat.ms_gestion_ventas.dto.prestamo.PrestamoResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Prestamo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    @Mapping(target = "idPrestamo", source = "idPrestamo")
    @Mapping(target = "idLibro", source = "libro.idLibro")
    @Mapping(target = "tituloLibro", source = "libro.titulo")
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "username", source = "usuario.username")
    PrestamoResponse toResponse(Prestamo prestamo);

    List<PrestamoResponse> toResponseList(List<Prestamo> prestamos);
}
