package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.auth.LoginRequestDTO;
import com.kr.libraryapiassignment.dto.auth.LoginResponseDTO;
import com.kr.libraryapiassignment.dto.auth.LogoutResponseDTO;
import com.kr.libraryapiassignment.dto.auth.SelfResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto,
                                                               HttpServletRequest request) {
        return authService.login(dto, request).toEntity();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponseDTO>> logout(HttpServletRequest request) {
        return authService.logout(request).toEntity();
    }

    @GetMapping("/self")
    public ResponseEntity<ApiResponse<SelfResponseDTO>> self(Authentication auth) {
        return authService.getSelf(auth).toEntity();
    }
}
