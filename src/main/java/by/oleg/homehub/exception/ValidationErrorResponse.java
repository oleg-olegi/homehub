package by.oleg.homehub.exception;

public record ValidationErrorResponse(
        String field,
        String message
) {
}