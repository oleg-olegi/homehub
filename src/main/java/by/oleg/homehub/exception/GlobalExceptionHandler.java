package by.oleg.homehub.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e) {

        log.warn("Illegal argument: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                List.of(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                dateTimeFormat()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleBadCredentials(
            BadCredentialsException e) {

        log.warn("Bad credentials: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(
                List.of(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                dateTimeFormat()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {

        log.warn("Validation failed: {}", e.getMessage());

        List<ValidationErrorResponse> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse response = new ErrorResponse(
                errors,
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                dateTimeFormat()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    private String dateTimeFormat() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}