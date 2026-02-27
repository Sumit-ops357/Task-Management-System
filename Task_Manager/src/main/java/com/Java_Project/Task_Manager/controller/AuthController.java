package com.Java_Project.Task_Manager.controller;

//Sumit verifing the changes 

import com.Java_Project.Task_Manager.dto.LoginRequest;
import com.Java_Project.Task_Manager.dto.RegisterRequest;
import com.Java_Project.Task_Manager.entity.User;
import com.Java_Project.Task_Manager.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request)
    {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request)
    {
        return authService.login(request.getUsername(), request.getPassword());
    }
}

//Sumit is Coding Java development