package com.conexa.saude.plataforma_medicos.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    public ValidationException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
