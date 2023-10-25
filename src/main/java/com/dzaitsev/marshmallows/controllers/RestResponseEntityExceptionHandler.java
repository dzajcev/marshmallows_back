package com.dzaitsev.marshmallows.controllers;

import com.dzaitsev.marshmallows.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {OrderNotFoundException.class, ClientNotFoundException.class, PriceNotFoundException.class,
            PriceNotFoundException.class, GoodNotFoundException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = {DeleteDeliveryNotAllowException.class, DeleteOrderNotAllowException.class})
    protected ResponseEntity<Object> deleteError(
            RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {AuthorizationException.class})
    protected ResponseEntity<Object> forbidden(
            AuthorizationException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorMessage(ex.getErrorCodes(), ex.getMessage()),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorMessage {
        private ErrorCodes code;
        private String text;
    }
}