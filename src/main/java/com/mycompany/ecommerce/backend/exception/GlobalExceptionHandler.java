package com.ecommerce.exception;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String,Object>> handleApiException(ApiException ex, WebRequest req) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now(), "message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now(), "message", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
