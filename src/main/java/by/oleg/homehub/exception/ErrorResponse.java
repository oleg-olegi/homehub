package by.oleg.homehub.exception;

import java.util.List;

public record ErrorResponse(
        List<ValidationErrorResponse> errors,
        String message,
        int status,
        String timestamp) {
}
