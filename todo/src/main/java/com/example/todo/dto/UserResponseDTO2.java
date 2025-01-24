package com.example.todo.dto;

import java.util.List;

public record UserResponseDTO2(
                Long userId,
                String username,
                String email,
                String role,
                List<TodoResponse> todoResponseDTOs

) {

}
