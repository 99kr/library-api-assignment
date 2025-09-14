package com.kr.libraryapiassignment.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.libraryapiassignment.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.addError("Access Denied");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
