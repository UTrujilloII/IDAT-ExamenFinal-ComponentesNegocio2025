
package pe.edu.idat.ms_biblioteca.dto;

public record LibroDTO(
        Long id,
        String titulo,
        String autor,
        String categoria,
        String codigo,
        Integer stockTotal,
        Integer stockDisponible
) {}
