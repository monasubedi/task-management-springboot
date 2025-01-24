package com.example.todo.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.todo.service.UserService;
import com.example.todo.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (antPathMatcher.match("/api/auth/**", uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        System.out.println("header is" + header);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid JWT header");
        }
        String token = header.substring(7);
        String email = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        if (email == null) {
            throw new BadCredentialsException("Invalid JWT token, not email");
        }
        UserDetails user = userService.loadUserByUsername(email);

        if (user == null) {
            throw new BadCredentialsException("Invalid JWT token, not user");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        System.out.println("auth is " + auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}