package com.conexa.saude.plataforma_medicos.exceptions;

import com.conexa.saude.plataforma_medicos.dto.ErrorMessageDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessageDTO> handleApiException(ApiException ex) {
        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex instanceof FaultException faultexception ? faultexception.getDetails() : null)
                .build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> String.format("Campo '%s': %s",
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .toList();

        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .code("API-PLATAFORMA-MEDICOS-VALIDATION-ERROR")
                .message("Houve erros de validação nos dados enviados.")
                .details(details)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleGeneralException(Exception ex) {
        ErrorMessageDTO error = ErrorMessageDTO.builder()
                .code("API-PLATAFORMA-MEDICOS-UNKNOWN")
                .message("Ocorreu um erro inesperado. Entre em contato com o suporte.")
                .details(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

