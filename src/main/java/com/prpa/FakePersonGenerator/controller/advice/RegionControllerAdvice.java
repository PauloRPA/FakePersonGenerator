package com.prpa.FakePersonGenerator.controller.advice;

import com.prpa.FakePersonGenerator.model.exceptions.RegionExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegionControllerAdvice {

    @ExceptionHandler(RegionExistsException.class)
    public ProblemDetail catchRegionExists(RegionExistsException ex) {
        String message = "The region '%s' already exists.".formatted(ex.getRegionName());
        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        pb.setTitle(ex.getRegionName());
        return pb;
    }
}
