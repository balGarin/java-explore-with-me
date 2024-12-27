package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice(value = "ru.practicum")
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandle(NotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, "The required object was not found.",
                exception.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class,DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse constraintViolationExceptionHandle(Throwable exception) {
        return new ErrorResponse(HttpStatus.CONFLICT, "Integrity constraint has been violated.",
                exception.getMessage(), LocalDateTime.now().format(formatter));
    }


    @ExceptionHandler(value = {ValidationException.class,
            MethodArgumentNotValidException.class,
            NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidate(Throwable exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST,"Incorrectly made request.",
                exception.getMessage(),LocalDateTime.now().format(formatter) );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectDateException(IncorrectDateException exception){
        return new ErrorResponse(HttpStatus.BAD_REQUEST,"Incorrectly made request.",
                exception.getMessage(),LocalDateTime.now().format(formatter) );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException exception){
        return new ErrorResponse(HttpStatus.CONFLICT, "Integrity constraint has been violated.",
                exception.getMessage(), LocalDateTime.now().format(formatter));
    }



}
