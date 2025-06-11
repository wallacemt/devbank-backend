package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.request.StashRequestDTO;
import com.devbank.DevBank.dtos.response.StashResponseDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.InsuficientAmountException;
import com.devbank.DevBank.services.StashService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/stash")
public class StashController {
    @Autowired
    private StashService stashService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StashRequestDTO data, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.status(201).body(stashService.create(data, user));
        } catch (InsuficientAmountException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(stashService.findAll(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(stashService.findById(id, user));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid StashRequestDTO data, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(stashService.update(id, data, user));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<?> depositAmount(@PathVariable UUID id, @RequestBody Map<String, Double> valueMap, @AuthenticationPrincipal User user) {
        double value = valueMap.get("value");
        try {
            return ResponseEntity.ok(stashService.addAmount(id, value, user));
        } catch (InsuficientAmountException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/loot")
    public ResponseEntity<?> lootAmount(@PathVariable UUID id, @RequestBody Map<String, Double> valueMap, @AuthenticationPrincipal User user) {
        double value = valueMap.get("value");
        try {
            return ResponseEntity.ok(stashService.getAmount(id, user, value));
        } catch (InsuficientAmountException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(Map.of("message", stashService.delete(id, user)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
