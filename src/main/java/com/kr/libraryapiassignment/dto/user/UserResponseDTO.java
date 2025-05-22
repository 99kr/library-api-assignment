package com.kr.libraryapiassignment.dto.user;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record UserResponseDTO(Long id, String firstName, String lastName, String email,
                              LocalDateTime registrationDate) {
    @Override
    @NonNull
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
