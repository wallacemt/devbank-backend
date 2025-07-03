package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.request.CreateCardRequestDTO;
import com.devbank.DevBank.dtos.response.CardResponseDTO;
import com.devbank.DevBank.dtos.response.PreApprovedLimitResponseDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.services.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;


    @GetMapping("/pre_approved")
    public ResponseEntity<?> generatePreApprovedLimit(@AuthenticationPrincipal User user) {
        try {
            double limit = cardService.generatePreApprovedLimit(user);
            PreApprovedLimitResponseDTO responseDTO = new PreApprovedLimitResponseDTO(limit);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateCard(@AuthenticationPrincipal User user, @RequestBody Map<String, String> valueMap) {
        String value = valueMap.get("expiresFatureDate");
        try {
            return ResponseEntity.ok(cardService.createCardWithPreApprovedLimit(user, value));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createManualCard(@AuthenticationPrincipal User user, @RequestBody CreateCardRequestDTO requestDTO) {
        try {
            return ResponseEntity.ok(cardService.createManualCard(user, requestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getUserCards(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cardService.getUserCards(user));
    }

  

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        try {
            cardService.deleteCard(id, user);
            return ResponseEntity.ok(Map.of("message", "Cartão excluído com sucesso!"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

