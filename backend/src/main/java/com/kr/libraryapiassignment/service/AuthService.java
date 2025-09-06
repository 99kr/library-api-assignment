package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.auth.LoginRequestDTO;
import com.kr.libraryapiassignment.dto.auth.LoginResponseDTO;
import com.kr.libraryapiassignment.dto.auth.LogoutResponseDTO;
import com.kr.libraryapiassignment.dto.auth.SelfResponseDTO;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.repository.UserRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO dto, HttpServletRequest request) {
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        Authentication auth = authenticationManager.authenticate(token);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return response.setData(new LoginResponseDTO(session.getId()));
    }

    public ApiResponse<LogoutResponseDTO> logout(HttpServletRequest request) {
        ApiResponse<LogoutResponseDTO> response = new ApiResponse<>();

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return response.setData(new LogoutResponseDTO(true));
    }

    public ApiResponse<SelfResponseDTO> getSelf(Authentication auth) {
        ApiResponse<SelfResponseDTO> response = new ApiResponse<>();

        if (auth == null) {
            return response.setData(new SelfResponseDTO(false));
        }

        Optional<User> optUser = userRepository.findByEmailIgnoreCase(auth.getName());

        if (optUser.isEmpty()) {
            return response.setData(new SelfResponseDTO(false));
        }

        User user = optUser.get();

        return response
                .setData(new SelfResponseDTO(user.getFirstName(), user.getLastName(), user.getEmail(), true));
    }
}
