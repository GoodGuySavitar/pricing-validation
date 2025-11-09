package com.pricing.pricing_proj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    //GLOBAL ERROR HANDLER FOR THE CONTROLLER
    // 400: BAD INPUT (thrown intentionally)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatus(ResponseStatusException ex) {
        return json(ex.getStatusCode().value(), ex.getReason());
    }

    // 400: file upload problems, size limits, etc.
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipart(MultipartException ex) {
        return json(HttpStatus.BAD_REQUEST.value(), "Invalid file upload: " + ex.getMessage());
    }

    // 400: bad params in general
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return json(HttpStatus.BAD_REQUEST.value(), "Bad request: " + ex.getMessage());
    }

    // 500: everything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception ex) {
        return json(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error: " + ex.getMessage());
    }

    private ResponseEntity<Map<String,Object>> json(int status, String message) {
        return ResponseEntity.status(status).body(Map.of(
            "timestamp", Instant.now().toString(),
            "status", status,
            "message", message
        ));
    }
}
