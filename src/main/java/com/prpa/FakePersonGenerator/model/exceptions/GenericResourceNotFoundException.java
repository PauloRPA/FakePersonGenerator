package com.prpa.FakePersonGenerator.model.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class GenericResourceNotFoundException extends RuntimeException{

    public GenericResourceNotFoundException(String message) {
        super(message);
    }

}
