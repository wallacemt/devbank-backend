package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.request.CreateDepositRequestDTO;
import com.devbank.DevBank.dtos.response.ConfirmDepositResponseDTO;
import com.devbank.DevBank.dtos.response.CreateDepositResponseDTO;
import com.devbank.DevBank.dtos.response.DepositResponseDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.DepositHasCompletedException;
import com.devbank.DevBank.exeptions.TransactionNotFountExeception;
import com.devbank.DevBank.services.DepositService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping
    public ResponseEntity<CreateDepositResponseDTO> createDeposit(
            @RequestBody CreateDepositRequestDTO request,
            @AuthenticationPrincipal User user
    ) {
        CreateDepositResponseDTO response = depositService.createDeposit(user, request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{depositId}")
    public ResponseEntity<?> getDepositInfo(@PathVariable UUID depositId, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(depositService.getDepositById(depositId, user));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (DepositHasCompletedException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{depositId}/confirm")
    public ResponseEntity<?> confirmDeposit(@PathVariable UUID depositId) {
        try {
            ConfirmDepositResponseDTO response = depositService.confirmDeposit(depositId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (DepositHasCompletedException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

