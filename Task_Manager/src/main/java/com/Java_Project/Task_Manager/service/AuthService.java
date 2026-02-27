package com.Java_Project.Task_Manager.service;

import com.Java_Project.Task_Manager.entity.User;
import com.Java_Project.Task_Manager.repository.UserRepository;
import com.Java_Project.Task_Manager.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;  //Database access
    private final PasswordEncoder passwordEncoder; //Password Encryption
    private final JwtUtil jwtUtil;  //Jwt token generation
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(User user)
    {
        //Check if username already exists
        if(userRepository.findByUsername(user.getUsername()).isPresent())
        {
            throw new RuntimeException("Username already exists");
        }

        //Validate password
        if(user.getPassword() == null || user.getPassword().isEmpty())
        {
            throw new RuntimeException("Password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "User registered Successfully";
    }

    public String login(String username, String password)
    {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(password, user.getPassword()))
        {
            return jwtUtil.generateToken(username);
        }

        throw new RuntimeException("Invalid credentials");
    }
}
