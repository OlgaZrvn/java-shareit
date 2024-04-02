package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void shouldHandleNotFoundException() {
        ErrorResponse result = errorHandler.handleNotFoundException(new NotFoundException("Error"));
        assertEquals(result.getError(), "Error");
    }

    @Test
    void shouldHandleValidationException() {
        ErrorResponse result = errorHandler.handleValidationException(new ValidationException("Error"));
        assertEquals(result.getError(), "Error");
    }

    @Test
    void shouldHandleInternalErrorException() {
        ErrorResponse result = errorHandler.handleInternalErrorException(new InternalErrorException("Error"));
        assertEquals(result.getError(), "Error");
    }

}