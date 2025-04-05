package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.UserRegisterDTO;
import com.devbank.DevBank.exeptions.CpfAlreadyRegisteredException;
import com.devbank.DevBank.exeptions.EmailAlreadyRegisteredException;
import com.devbank.DevBank.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO data) {
        try {
            Map<String, String> response = userAuthService.userRegister(data);
            return ResponseEntity.status(201).body(response.get("message"));
        } catch (EmailAlreadyRegisteredException | CpfAlreadyRegisteredException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno do servidor: " + e.getMessage());
        }
    }

}