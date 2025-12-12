package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.prestamo.PrestamoResponse;
import com.biblioteca.biblioteca_api.entity.Prestamo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-30T23:35:01-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class PrestamoMapperImpl implements PrestamoMapper {

    @Override
    public PrestamoResponse toResponseDTO(Prestamo prestamo) {
        if ( prestamo == null ) {
            return null;
        }

        PrestamoResponse.PrestamoResponseBuilder prestamoResponse = PrestamoResponse.builder();

        prestamoResponse.id( prestamo.getId() );
        prestamoResponse.fechaPrestamo( prestamo.getFechaPrestamo() );
        prestamoResponse.fechaDevolucionEstimada( prestamo.getFechaDevolucionEstimada() );
        prestamoResponse.fechaDevolucionReal( prestamo.getFechaDevolucionReal() );

        prestamoResponse.usuario( prestamo.getUsuario().getNombre() );
        prestamoResponse.libro( prestamo.getLibro().getTitulo() );
        prestamoResponse.estado( prestamo.getEstado().name() );

        return prestamoResponse.build();
    }
}
