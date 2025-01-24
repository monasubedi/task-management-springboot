package com.example.todo.dto;

public record TodoRequestDTO(
                String task,
                Boolean isCompleted,
                String priority) {

}
