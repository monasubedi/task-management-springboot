package com.example.todo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.todo.dto.UserRequestDTO;
import com.example.todo.dto.UserResponseDTO;

public interface UserService extends UserDetailsService {
    Long createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long userId);

    UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO);

    void removeUser(Long userId);

}
