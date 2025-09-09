package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException exception) throws AccessDeniedException {
        // Forward it to AuthAccessDeniedHandler
        throw exception;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.addError("Bad credentials").setStatusCode(HttpStatus.BAD_REQUEST);

        return response.toEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception exception) {
        ApiResponse<Void> response = new ApiResponse<>();

        response.addError(exception.getMessage())
                .setStatusCode(getStatusCodeForException(exception));

        return response.toEntity();
    }

    private HttpStatus getStatusCodeForException(Exception exception) {
        if (exception instanceof ErrorResponse) {
            HttpStatusCode statusCode = ((ErrorResponse) exception).getStatusCode();
            return HttpStatus.valueOf(statusCode.value());
        }

        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null)
            return responseStatus.code();


        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
