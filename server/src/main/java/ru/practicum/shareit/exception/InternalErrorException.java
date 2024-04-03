package ru.practicum.shareit.exception;

public class InternalErrorException extends InternalError {

    public InternalErrorException(String message) {
        super(message);
    }
}