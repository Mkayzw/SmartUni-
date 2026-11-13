package com.smartuni.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralised translation from Java exceptions to HTTP responses.
 *
 * Controllers and services throw meaningful exceptions; this class decides the status code and body.
 * That keeps HTTP concerns out of the service layer.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        ApiError body = ApiError.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Placeholder endpoints throw UnsupportedOperationException("TODO-...").
     * Mapping it to 501 lets the React app show "not implemented yet" instead of a generic 500.
     * Delete this handler once you have finished all the exercises if you like.
     */
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiError> handleNotImplemented(UnsupportedOperationException exception, HttpServletRequest request) {
        ApiError body = ApiError.of(
                HttpStatus.NOT_IMPLEMENTED.value(),
                HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(body);
    }

    // TODO-BE-003 (part 1): Handle BusinessRuleViolationException.
    //   Right now it falls through to Spring's default handling and the client receives a 500.
    //   A broken business rule is not a server crash. Pick the correct 4xx status and return an ApiError.

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiError> handleBusinessRuleViolation(
            BusinessRuleViolationException exception, HttpServletRequest request) {
        ApiError body = ApiError.of(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // TODO-BE-003 (part 2): Handle bean validation failures from @Valid @RequestBody.
    //   Which exception does Spring MVC throw when @Valid fails on a request body?
    //   Return 400 with ApiError.fieldErrors populated: field name -> message.
    //   Tests: StudentControllerTest, EnrollmentControllerIntegrationTest
}
