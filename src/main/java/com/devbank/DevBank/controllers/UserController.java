package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.response.UserProfileRequestDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.BonusHasReclaimException;
import com.devbank.DevBank.exeptions.IncompleteProfileException;
import com.devbank.DevBank.exeptions.PerfilAlreadyRegisteredException;
import com.devbank.DevBank.services.ProfileService;
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

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(userService.getUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<?> postUserProfile(@AuthenticationPrincipal User user, @RequestBody UserProfileRequestDTO data) {
        try {
            return ResponseEntity.ok(profileService.createUserProfile(user, data));
        } catch (PerfilAlreadyRegisteredException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/reclaim-bonus")
    public ResponseEntity<?> userReclaimBonus(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(userService.reclaimBonus(user));
        } catch (IncompleteProfileException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (BonusHasReclaimException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}

