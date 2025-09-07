package com.kr.libraryapiassignment.mapper;

import com.kr.libraryapiassignment.dto.user.*;
import com.kr.libraryapiassignment.entity.Role;
import com.kr.libraryapiassignment.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {
    private final BookMapper bookMapper;
    private final LoanMapper loanMapper;

    public UserMapper(BookMapper bookMapper, LoanMapper loanMapper) {
        this.bookMapper = bookMapper;
        this.loanMapper = loanMapper;
    }

    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                                   user.getRegistrationDate(), user.getRoles());
    }

    public List<UserResponseDTO> toDTO(List<User> users) {
        return users.stream().map(this::toDTO).toList();
    }

    public UserLoanResponseDTO toDTOLoan(UserLoanTransientDTO dto) {
        return new UserLoanResponseDTO(loanMapper.toDTOBase(dto.loan()), bookMapper.toDTOMinimal(dto.book()));
    }

    public List<UserLoanResponseDTO> toDTOLoan(List<UserLoanTransientDTO> dtos) {
        return dtos.stream().map(this::toDTOLoan).toList();
    }

    public UserMinimalResponseDTO toDTOMinimal(User user) {
        return new UserMinimalResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public User toEntity(UserRequestDTO dto) {
        return new User(null, dto.firstName(), dto.lastName(), dto.email().toLowerCase(), dto.password(),
                        LocalDateTime.now(), Set.of());
    }
}
