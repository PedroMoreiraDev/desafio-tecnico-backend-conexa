package com.conexa.saude.plataforma_medicos.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends ApiException {
    public InternalServerException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
