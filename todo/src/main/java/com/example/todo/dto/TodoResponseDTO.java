package com.example.todo.dto;

public record TodoResponseDTO(
                Long id,
                String task,
                Boolean isCompleted,
                String priority,
                UserResponseDTO userResponseDTO) {

}
