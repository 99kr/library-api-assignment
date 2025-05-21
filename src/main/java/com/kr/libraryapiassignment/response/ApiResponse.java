package com.kr.libraryapiassignment.response;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@JsonIncludeProperties({ "errors", "data" })
public class ApiResponse<T> {
    private final List<ApiResponseError> errors = new ArrayList<>();
    private T data;
    private HttpStatusCode statusCode = HttpStatus.OK;

    public List<ApiResponseError> getErrors() {
        return errors;
    }

    public ApiResponse<T> addError(String field, String message) {
        errors.add(new ApiResponseError(field, message));
        return this;
    }

    public ApiResponse<T> addError(String message) {
        errors.add(new ApiResponseError(null, message));
        return this;
    }

    public ApiResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResponse<T> setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public int errorCount() {
        return errors.size();
    }

    public ResponseEntity<ApiResponse<T>> toEntity() {
        return ResponseEntity.status(statusCode).body(this);
    }
}
