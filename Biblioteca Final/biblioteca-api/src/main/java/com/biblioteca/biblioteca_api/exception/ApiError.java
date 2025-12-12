package com.biblioteca.biblioteca_api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

    private int status;
    private String error;
    private String message;
    private String path;

    // Formato estándar ISO → más compatible con frontends
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
