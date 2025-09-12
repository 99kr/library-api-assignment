package com.kr.libraryapiassignment.controller;

import com.kr.libraryapiassignment.dto.user.UserLoanResponseDTO;
import com.kr.libraryapiassignment.dto.user.UserRequestDTO;
import com.kr.libraryapiassignment.dto.user.UserResponseDTO;
import com.kr.libraryapiassignment.response.ApiResponse;
import com.kr.libraryapiassignment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email).toEntity();
    }

    @GetMapping("/{userId}/loans")
    public ResponseEntity<ApiResponse<List<UserLoanResponseDTO>>> findUserLoans(@PathVariable Long userId) {
        return userService.findUserLoansById(userId).toEntity();
    }

    /*
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> addUser(@RequestBody UserRequestDTO dto) {
        return userService.save(dto).toEntity();
    }*/
}
