package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.UserRequestDTO;
import com.example.todo.dto.UserResponse;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.UserServiceImpl;
import com.example.todo.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        var userId = userService.createUser(userRequestDTO);
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.email(), userRequestDTO.password()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with this id."));
        SecurityContextHolder.getContext().setAuthentication(auth);
        var token = jwtUtil.generateToken(auth, user.getRole());
        UserResponse userResponse = new UserResponse(userId, token);

        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO userRequestDTO) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.email(), userRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = userRepository.findUserByEmail(userRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("No user with this user email."));
        var token = jwtUtil.generateToken(auth, user.getRole());

        UserResponse userResponse = new UserResponse(user.getUserId(), token);

        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }
}
