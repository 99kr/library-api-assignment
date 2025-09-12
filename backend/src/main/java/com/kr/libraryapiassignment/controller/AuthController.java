package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.auth.*;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.AuthService;
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
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto).toEntity();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponseDTO>> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return authService.refresh(refreshToken).toEntity();
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponseDTO>> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return authService.logout(refreshToken).toEntity();
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@RequestBody RegisterRequestDTO dto) {
        return authService.register(dto).toEntity();
    }

    @GetMapping("/self")
    public ResponseEntity<ApiResponse<SelfResponseDTO>> self(Authentication auth) {
        return authService.getSelf(auth).toEntity();
    }
}
