package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.UserRequestDTO;
import com.example.todo.dto.UserResponseDTO;
import com.example.todo.service.UserServiceImpl;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(userId, userRequestDTO);
        return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.OK);
    }

    @PreAuthorize("haAnysRole('ADMIN','USER')")
    @GetMapping("/users")
    public ResponseEntity<?> getUserById(@PathVariable(required = false) Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);
        return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.removeUser(userId);
        return new ResponseEntity<UserResponseDTO>(HttpStatus.NO_CONTENT);
    }

}
