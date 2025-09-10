package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.auth.*;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.repository.UserRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.security.JwtUtils;
import com.kr.libraryapiassignment.security.RefreshTokenStatus;
import com.kr.libraryapiassignment.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService,
                       RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO dto) {
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        Authentication auth = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtUtils.generateAccessToken(auth);
        String refreshToken = jwtUtils.generateRefreshToken(auth);

        ResponseCookie refreshCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Duration.ofMillis(jwtUtils.getJwtRefreshExpirationMs()))
                .build();

        refreshTokenService.saveToken(
                refreshToken,
                jwtUtils.getJwtRefreshExpirationMs()
        );

        return response
                .addCookie(refreshCookie)
                .setData(new LoginResponseDTO(accessToken, jwtUtils.getJwtRefreshExpirationMs()));
    }

    public ApiResponse<RefreshResponseDTO> refresh(@Nullable String refreshToken) {
        ApiResponse<RefreshResponseDTO> response = new ApiResponse<>();

        if (refreshToken == null) {
            return response.addError("Invalid refresh token").setStatusCode(HttpStatus.UNAUTHORIZED);
        }

        boolean isValid = jwtUtils.isValidRefreshToken(refreshToken);
        RefreshTokenStatus tokenStatus = refreshTokenService.getTokenStatus(refreshToken);

        // clean up if the token has expired
        if (tokenStatus == RefreshTokenStatus.EXPIRED) {
            refreshTokenService.deleteToken(refreshToken);
        }

        if (!isValid || tokenStatus != RefreshTokenStatus.VALID) {
            return response.addError("Invalid refresh token").setStatusCode(HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtils.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                                                                userDetails.getAuthorities());

        String accessToken = jwtUtils.generateAccessToken(authentication);

        return response.setData(new RefreshResponseDTO(accessToken));
    }

    public ApiResponse<LogoutResponseDTO> logout(@Nullable String refreshToken) {
        ApiResponse<LogoutResponseDTO> response = new ApiResponse<>();

        if (refreshToken == null) {
            return response.setData(new LogoutResponseDTO(false)).setStatusCode(HttpStatus.BAD_REQUEST);
        }

        refreshTokenService.deleteToken(refreshToken);

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
