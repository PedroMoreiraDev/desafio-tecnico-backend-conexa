package com.conexa.saude.plataforma_medicos.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {
    public BusinessException(ErrorMessage error) {
        super(error.getCode(), error.getMessage(), HttpStatus.CONFLICT);
    }

}
