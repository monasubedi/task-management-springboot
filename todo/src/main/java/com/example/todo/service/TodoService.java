package com.example.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.todo.dto.TodoRequestDTO;
import com.example.todo.dto.TodoResponse;
import com.example.todo.dto.TodoResponseDTO;
import com.example.todo.dto.UserResponseDTO;
import com.example.todo.dto.UserResponseDTO2;
import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;

@Service
public class TodoService {
        @Autowired
        private TodoRepository todoRepository;
        @Autowired
        private UserRepository userRepository;

        public TodoResponseDTO createTask(TodoRequestDTO todoRequestDTO) {
                var username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findUserByEmail(username)
                                .orElseThrow(() -> new RuntimeException("No user found with this user name"));
                Todo todo = new Todo(null, todoRequestDTO.task(), todoRequestDTO.isCompleted(),
                                todoRequestDTO.priority(), user);

                var savedTodo = todoRepository.save(todo);
                System.out.println("saved to do is" + savedTodo.getTask());
                user.getTodos().add(savedTodo);
                TodoResponseDTO todoResponseDTO = new TodoResponseDTO(savedTodo.getId(), savedTodo.getTask(),
                                savedTodo.getIsCompleted(), savedTodo.getPriority(),
                                new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(),
                                                user.getRole()));
                return todoResponseDTO;

        }

        public TodoResponseDTO updateTask(TodoRequestDTO todoRequestDTO, Long todoId) {
                var username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findUserByEmail(username)
                                .orElseThrow(() -> new RuntimeException("No user found with this user name"));

                Todo savedTodo = todoRepository.findById(todoId)
                                .orElseThrow(() -> new RuntimeException("No task found with this id."));
                savedTodo.setTask(todoRequestDTO.task());
                savedTodo.setPriority(todoRequestDTO.priority());
                savedTodo.setIsCompleted(todoRequestDTO.isCompleted());
                Todo updatedTodo = todoRepository.save(savedTodo);
                TodoResponseDTO todoResponseDTO = new TodoResponseDTO(updatedTodo.getId(), updatedTodo.getTask(),
                                updatedTodo.getIsCompleted(), updatedTodo.getPriority(),
                                new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(),
                                                user.getRole()));
                return todoResponseDTO;

        }

        public TodoResponseDTO getTask(Long taskId) {
                Todo task = todoRepository.findById(taskId)
                                .orElseThrow(() -> new RuntimeException("No task found with this id."));
                TodoResponseDTO todoResponseDTO = new TodoResponseDTO(task.getId(), task.getTask(),
                                task.getIsCompleted(), task.getPriority(), new UserResponseDTO(
                                                task.getUser().getUserId(), task.getUser().getUsername(),
                                                task.getUser().getEmail(), task.getUser().getRole()));

                return todoResponseDTO;
        }

        public void deleteTask(Long taskId) {
                var username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findUserByEmail(username)
                                .orElseThrow(() -> new RuntimeException("No user found with this user name"));

                var todo = todoRepository.findById(taskId)
                                .orElseThrow(() -> new RuntimeException("No task found with this id."));
                if (todo != null) {
                        todoRepository.delete(todo);
                }

        }

        public UserResponseDTO2 getAllTasks() {
                var username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findUserByEmail(username)
                                .orElseThrow(() -> new RuntimeException("No user found with this user name"));
                UserResponseDTO2 userResponseDTO2 = new UserResponseDTO2(user.getUserId(), user.getUsername(),
                                user.getEmail(), user.getRole(),
                                user.getTodos().stream().map(t -> new TodoResponse(t.getId(), t.getTask(),
                                                t.getIsCompleted(), t.getPriority())).toList());

                return userResponseDTO2;

        }
}
