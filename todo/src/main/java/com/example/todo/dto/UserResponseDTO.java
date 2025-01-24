package com.example.todo.dto;

public record UserResponseDTO(
                Long userId,
                String username,
                String email,
                String role

) {

}
