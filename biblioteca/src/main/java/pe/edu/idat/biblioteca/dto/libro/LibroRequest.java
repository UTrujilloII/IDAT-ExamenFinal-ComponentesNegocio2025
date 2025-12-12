package pe.edu.idat.biblioteca.dto.libro;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.EmbeddableInstantiator;

public record LibroRequest(
        @NotBlank(message = "el campo isbn es obligatorio")
        @Size(min = 10, max = 13,message = "solo puede tener 13 caracteres como maximo")
        String isbn,

        @NotBlank(message = "el campo titulo es obligatorio")
        @Size(min = 3,max = 200,message ="el titulo puede tener entre 3 a 200 caracteres" )
        String titulo,

        @Size(max = 500, message = "La descripción puede tener hasta 500 caracteres")
        String descripcion,

        @NotBlank(message = "el campo autor es obligatorio")
        String autor,

        @NotBlank(message = "el campo genero es obligatorio")
        String genero,

        @NotNull(message = "el campo stock es obligatorio")
        @Min(0)
        @Max(20)
        Integer stock
) {}
