package com.lockerpudo.api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class) ResponseEntity<?> handle(ApiException e) { return ResponseEntity.status(e.getStatus()).body(Map.of("timestamp", Instant.now(), "message", e.getMessage())); }
    @ExceptionHandler(IllegalStateException.class) ResponseEntity<?> state(IllegalStateException e) { return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now(), "message", e.getMessage())); }
}