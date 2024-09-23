package com.prpa.FakePersonGenerator.model.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private final String value;
    private final String field;

    public AlreadyExistsException(String message) {
        super(message);
        field = "";
        value = "";
    }

    public AlreadyExistsException(String field, String value) {
        super("");
        this.field = field;
        this.value = value;
    }

}
