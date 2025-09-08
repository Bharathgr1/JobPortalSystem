package com.example.elevateJobPortal.controller;

import com.example.elevateJobPortal.entity.User;
import com.example.elevateJobPortal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        userService.registerUser(user);
        model.addAttribute("message", "User registered successfully!");
        return "auth/login";
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // Thymeleaf template "index.html"
    }

}