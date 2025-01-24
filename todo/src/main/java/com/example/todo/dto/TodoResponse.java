package com.example.todo.dto;

public record TodoResponse(
                Long id,
                String task,
                Boolean isCompleted,
                String priority) {

}
