package com.conexa.saude.plataforma_medicos.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    protected ApiException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

