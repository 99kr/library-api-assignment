package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.auth.*;
import com.kr.libraryapiassignment.dto.user.UserRequestDTO;
import com.kr.libraryapiassignment.dto.user.UserResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.security.audit.AuditLogAction;
import com.kr.libraryapiassignment.security.audit.AuditLogger;
import com.kr.libraryapiassignment.security.jwt.JwtUtils;
import com.kr.libraryapiassignment.security.jwt.RefreshTokenStatus;
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
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final AuditLogger auditLogger;
    private final BruteForceService bruteForceService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils,
                       UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService,
                       AuditLogger auditLogger, BruteForceService bruteForceService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.auditLogger = auditLogger;
        this.bruteForceService = bruteForceService;
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO dto) {
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>();

        Optional<Long> optLockedTime = bruteForceService.getLockedTime(dto.email());
        if (optLockedTime.isPresent()) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(optLockedTime.get());

            return response
                    .addError("Too many attempts, please wait " + seconds + " seconds.")
                    .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        }

        try {
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

            auditLogger.log(dto.email(), AuditLogAction.LOGIN, "/auth/login");
            bruteForceService.loginSucceeded(dto.email());

            return response
                    .addCookie(refreshCookie)
                    .setData(new LoginResponseDTO(accessToken, jwtUtils.getJwtRefreshExpirationMs()));

        } catch (Exception e) {
            auditLogger.log(dto.email(), AuditLogAction.FAILED_LOGIN, "/auth/login");
            bruteForceService.loginFailed(dto.email());

            return response.addError("Bad credentials").setStatusCode(HttpStatus.BAD_REQUEST);
        }
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

        String email = jwtUtils.getUsernameFromToken(refreshToken);

        refreshTokenService.deleteToken(refreshToken);

        auditLogger.log(email, AuditLogAction.LOGOUT, "/auth/logout");

        return response.setData(new LogoutResponseDTO(true));
    }

    public ApiResponse<RegisterResponseDTO> register(RegisterRequestDTO dto) {
        ApiResponse<RegisterResponseDTO> response = new ApiResponse<>();

        ApiResponse<UserResponseDTO> saveResponse = userService.save(
                new UserRequestDTO(dto.firstName(), dto.lastName(), dto.email(), dto.password()));

        if (saveResponse.hasErrors()) {
            response = saveResponse.cast(); // Transfer errors etc

            auditLogger.log(dto.email(), AuditLogAction.FAILED_REGISTER, "/auth/register");

            return response.setData(new RegisterResponseDTO(false));
        }

        auditLogger.log(dto.email(), AuditLogAction.REGISTER, "/auth/register");

        return response.setData(new RegisterResponseDTO(true));
    }
}
