package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.auth.LoginRequestDTO;
import com.kr.libraryapiassignment.dto.auth.LoginResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authManager;

    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO dto,
                                                               HttpServletRequest request) {
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        Authentication auth = authManager.authenticate(token);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        response.setData(new LoginResponseDTO(session.getId()));

        return response.toEntity();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me(Authentication auth) {
        ApiResponse<String> response = new ApiResponse<>();

        if (auth == null) {
            response.addError("Not logged in").setStatusCode(HttpStatus.UNAUTHORIZED);
        } else {
            response.setData(auth.getName());
        }

        return response.toEntity();
    }
}
