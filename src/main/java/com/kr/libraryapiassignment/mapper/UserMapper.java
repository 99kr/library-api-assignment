package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.user.UserRequestDTO;
import com.kr.libraryapiassignment.dto.user.UserResponseDTO;
import com.kr.libraryapiassignment.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                                   user.getRegistrationDate());
    }

    public List<UserResponseDTO> toDTO(List<User> users) {
        return users.stream().map(this::toDTO).toList();
    }

    public User toEntity(UserRequestDTO dto) {
        return new User(null, dto.firstName(), dto.lastName(), dto.email().toLowerCase(), dto.password(),
                        LocalDateTime.now());
    }
}
