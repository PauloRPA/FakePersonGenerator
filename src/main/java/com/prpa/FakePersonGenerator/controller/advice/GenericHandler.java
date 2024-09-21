package com.prpa.FakePersonGenerator.controller.advice;

import com.prpa.FakePersonGenerator.model.exceptions.AlreadyExistsException;
import com.prpa.FakePersonGenerator.model.exceptions.EmptyDatabaseException;
import com.prpa.FakePersonGenerator.model.exceptions.GenericResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail catchRegionExists(AlreadyExistsException ex) {
        String message = "The value %s in %s already exists".formatted(ex.getValue(), ex.getField());
        if (!ex.getMessage().isBlank()) message = ex.getMessage();

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        pb.setTitle(ex.getField() + " already exists.");
        return pb;
    }

    @ExceptionHandler(EmptyDatabaseException.class)
    public ProblemDetail catchEmptyDb(EmptyDatabaseException ex) {
        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pb.setTitle("Database empty.");
        return pb;
    }

    @ExceptionHandler(GenericResourceNotFoundException.class)
    public ErrorResponse catchNotFound(GenericResourceNotFoundException ex) {
        return ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
