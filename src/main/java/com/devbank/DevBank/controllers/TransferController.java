package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.request.TransferPixRequestDTO;
import com.devbank.DevBank.dtos.request.UserKeyRequestDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.*;
import com.devbank.DevBank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/transfer")
public class TransferController {
    @Autowired
    private TransactionsService transactionsService;

    @PostMapping("/user/key")
    public ResponseEntity<?> getUserByKey(@RequestBody UserKeyRequestDTO userKey, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(transactionsService.getUserByKey(userKey, user));
        } catch (UserByKeyNotFound e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/pix")
    public ResponseEntity<?> postUserPix(@RequestBody TransferPixRequestDTO data, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(transactionsService.postTranferByPix(data, user));
        } catch (InvalidUserTransactionPin | IncorrectUserTransactionPin | InvalidSenderTransactionsException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (UserByKeyNotFound | InsuficientAmountException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }
}
