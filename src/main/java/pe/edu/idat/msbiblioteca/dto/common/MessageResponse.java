package pe.edu.idat.msbiblioteca.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO genérico para respuestas con mensajes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
    private String status;
    private LocalDateTime timestamp;

    public MessageResponse(String message) {
        this.message = message;
        this.status = "success";
        this.timestamp = LocalDateTime.now();
    }

    public MessageResponse(String message, String status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public static MessageResponse success(String message) {
        return new MessageResponse(message, "success");
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, "error");
    }

    public static MessageResponse warning(String message) {
        return new MessageResponse(message, "warning");
    }

    public static MessageResponse info(String message) {
        return new MessageResponse(message, "info");
    }
}

