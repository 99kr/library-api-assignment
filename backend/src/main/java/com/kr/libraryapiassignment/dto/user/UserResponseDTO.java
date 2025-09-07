package com.kr.libraryapiassignment.dto.user;

import com.kr.libraryapiassignment.entity.Role;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(Long id, String firstName, String lastName, String email,
                              LocalDateTime registrationDate, Set<Role> roles) {
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
