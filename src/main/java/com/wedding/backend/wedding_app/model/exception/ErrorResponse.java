package com.wedding.backend.wedding_app.model.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String errorCode;
    private String errorMessage;
    private String errorReason;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private boolean retryable;
    private String errorId;
    private List<Detail> details;
}
