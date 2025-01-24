package com.example.todo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo.dto.UserRequestDTO;
import com.example.todo.dto.UserResponseDTO;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public Long createUser(UserRequestDTO userRequestDTO) {
        User user = new User(null, userRequestDTO.username(), userRequestDTO.email(), bCryptPasswordEncoder.encode(
                userRequestDTO.password()), userRequestDTO.role());

        User savedUser = userRepository.save(user);

        return savedUser.getUserId();

    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user;
        if (userId == null) {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            user = userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new RuntimeException("No user found with this user name"));

        } else {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("No user found with this userId"));
        }

        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getUsername(),
                    user.getEmail(), user.getRole());
            return userResponseDTO;
        }
        return null;
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this userId"));

        if (user != null) {
            user.setEmail(userRequestDTO.email());
            user.setUsername(userRequestDTO.username());

            User savedUser = userRepository.save(user);

            UserResponseDTO userResponseDTO = new UserResponseDTO(savedUser.getUserId(), savedUser.getUsername(),
                    savedUser.getEmail(), savedUser.getRole());

            return userResponseDTO;
        }
        return null;
    }

    @Override
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this userId"));
        if (user != null) {
            userRepository.delete(user);
        }
    }

    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user with this user email"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

}
