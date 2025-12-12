package com.proyecto.idat.ms_gestion_ventas.mappers;

import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.categoria.CategoriaResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "libros", ignore = true) // no se mapea desde el request
    Categoria toEntity(CategoriaRequest request);

    // Aqui solo mapeamos los campos que realmente existen en CategoriaResponse
    CategoriaResponse toResponse(Categoria categoria);
}
