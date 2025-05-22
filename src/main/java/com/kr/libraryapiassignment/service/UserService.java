package com.kr.libraryapiassignment.service;

import com.kr.libraryapiassignment.dto.user.UserRequestDTO;
import com.kr.libraryapiassignment.dto.user.UserResponseDTO;
import com.kr.libraryapiassignment.entity.User;
import com.kr.libraryapiassignment.mapper.UserMapper;
import com.kr.libraryapiassignment.repository.UserRepository;
import com.kr.libraryapiassignment.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public ApiResponse<UserResponseDTO> findByEmail(String email) {
        ApiResponse<UserResponseDTO> response = new ApiResponse<>();

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);

        if (userOpt.isEmpty())
            return response.addError(null, "No user found by email " + email).setStatusCode(HttpStatus.NOT_FOUND);

        return response.setData(userMapper.toDTO(userOpt.get()));
    }

    public ApiResponse<UserResponseDTO> save(UserRequestDTO dto) {
        ApiResponse<UserResponseDTO> response = new ApiResponse<>();

        if (dto.firstName() == null || dto.firstName().isBlank())
            response.addError("firstName", "Missing field 'firstName'.");

        if (dto.lastName() == null || dto.lastName().isBlank())
            response.addError("lastName", "Missing field 'lastName'.");

        if (dto.email() == null || dto.email().isBlank())
            response.addError("email", "Missing field 'email'.");
        else if (!EMAIL_REGEX.matcher(dto.email()).matches())
            response.addError("email", "Invalid email format.");
        else if (userRepository.existsByEmailIgnoreCase(dto.email()))
            response.addError("email", "A user with email '" + dto.email() + "' already exists.");

        if (dto.password() == null || dto.password().isBlank())
            response.addError("password", "Missing field 'password'");

        if (response.errorCount() > 0)
            return response.setStatusCode(HttpStatus.BAD_REQUEST);

        User user = userRepository.save(userMapper.toEntity(dto));

        return response.setData(userMapper.toDTO(user)).setStatusCode(HttpStatus.CREATED);
    }
}
