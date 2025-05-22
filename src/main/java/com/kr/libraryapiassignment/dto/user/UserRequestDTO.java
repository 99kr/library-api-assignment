package com.kr.libraryapiassignment.dto.user;

import org.springframework.lang.NonNull;

public record UserRequestDTO(String firstName, String lastName, String email, String password) {
    @Override
    @NonNull
    public String toString() {
        return "UserRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
