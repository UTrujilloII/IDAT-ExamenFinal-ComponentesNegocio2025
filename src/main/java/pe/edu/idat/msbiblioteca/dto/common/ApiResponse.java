package pe.edu.idat.msbiblioteca.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Clase DTO genérica para envolver las respuestas de la API REST.
 * Proporciona una estructura consistente para todas las respuestas del sistema.
 *
 * @param <T> Tipo de dato que contendrá la respuesta
 * @author Jonathan Jiménez
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * Indica si la operación fue exitosa
     */
    private boolean success;

    /**
     * Mensaje descriptivo de la respuesta
     */
    private String message;

    /**
     * Datos de la respuesta (pueden ser null si solo es un mensaje)
     */
    private T data;

    /**
     * Timestamp de cuando se generó la respuesta (en milisegundos desde epoch)
     */
    @Builder.Default
    private Long timestamp = Instant.now().toEpochMilli();

    /**
     * Método estático de conveniencia para crear una respuesta exitosa.
     *
     * @param message Mensaje de éxito
     * @param data Datos a retornar
     * @param <T> Tipo de dato
     * @return ApiResponse configurada como exitosa
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }

    /**
     * Método estático de conveniencia para crear una respuesta de error.
     *
     * @param message Mensaje de error
     * @param <T> Tipo de dato
     * @return ApiResponse configurada como error
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }

    /**
     * Método estático de conveniencia para crear una respuesta personalizada.
     *
     * @param success Indica si fue exitosa
     * @param message Mensaje descriptivo
     * @param data Datos a retornar
     * @param <T> Tipo de dato
     * @return ApiResponse configurada según parámetros
     */
    public static <T> ApiResponse<T> of(boolean success, String message, T data) {
        return ApiResponse.<T>builder()
                .success(success)
                .message(message)
                .data(data)
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }
}

