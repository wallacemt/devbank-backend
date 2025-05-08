package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.UserDTO;
import com.devbank.DevBank.dtos.UserRegisterDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(userService.getUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }
}

