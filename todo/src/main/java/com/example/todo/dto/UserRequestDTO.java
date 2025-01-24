package com.example.todo.dto;

public record UserRequestDTO(
                String username,
                String email,
                String password,
                String role) {

}
