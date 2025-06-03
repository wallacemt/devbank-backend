package com.devbank.DevBank.controllers;


import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.exeptions.TransactionNotFountExeception;
import com.devbank.DevBank.repositories.TransitionsRepository;
import com.devbank.DevBank.services.ReceiptService;
import com.devbank.DevBank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;


    @GetMapping("/{id}/receipt")
    public ResponseEntity<?> getReceipt(@PathVariable String id) {
        try {
            byte[] pdf = transactionsService.generateComprovante(UUID.fromString(id));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=comprovante-devbank.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (TransactionNotFountExeception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

    }


}
