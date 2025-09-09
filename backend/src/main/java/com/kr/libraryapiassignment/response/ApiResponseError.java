package com.kr.libraryapiassignment.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record ApiResponseError(@JsonInclude(Include.NON_NULL) String field, String message) {
}
