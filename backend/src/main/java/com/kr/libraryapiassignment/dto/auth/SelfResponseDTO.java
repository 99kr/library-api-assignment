package com.kr.libraryapiassignment.dto.auth;

public record SelfResponseDTO(String firstName, String lastName, String email, boolean isLoggedIn) {
    public SelfResponseDTO(boolean isLoggedIn) {
        this("", "", "", isLoggedIn);
    }
}
