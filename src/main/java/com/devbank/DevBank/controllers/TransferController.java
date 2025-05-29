package com.devbank.DevBank.controllers;

import com.devbank.DevBank.dtos.UserKeyRequestDTO;
import com.devbank.DevBank.exeptions.UserByKeyNotFound;
import com.devbank.DevBank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/transfer")
public class TransferController {
    @Autowired
    private TransactionsService transactionsService;

    @PostMapping("/user/key")
    public ResponseEntity<?> getUserByKey(@RequestBody UserKeyRequestDTO userKey) {
        try {
            return ResponseEntity.ok(transactionsService.getUserByKey(userKey));
        } catch (UserByKeyNotFound e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }
}
