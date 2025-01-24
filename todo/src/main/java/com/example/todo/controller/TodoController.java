package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.TodoRequestDTO;
import com.example.todo.dto.TodoResponseDTO;
import com.example.todo.dto.UserResponseDTO2;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO todoResponseDTO = todoService.createTask(todoRequestDTO);

        return new ResponseEntity<TodoResponseDTO>(todoResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@RequestBody TodoRequestDTO todoRequestDTO, @PathVariable Long taskId) {
        TodoResponseDTO todoResponseDTO = todoService.updateTask(todoRequestDTO, taskId);

        return new ResponseEntity<TodoResponseDTO>(todoResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId) {
        TodoResponseDTO todoResponseDTO = todoService.getTask(taskId);

        return new ResponseEntity<TodoResponseDTO>(todoResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        UserResponseDTO2 userResponseDTO2 = todoService.getAllTasks();
        return new ResponseEntity<UserResponseDTO2>(userResponseDTO2, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        todoService.deleteTask(taskId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
