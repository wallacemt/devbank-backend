package com.devbank.DevBank.controllers;


import com.devbank.DevBank.dtos.response.TransactionsResponseDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.TransactionNotFountExeception;
import com.devbank.DevBank.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/history")
    public Page<TransactionsResponseDTO> historyByDay(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable) {
        if(date == null) {
            date = LocalDate.now();
        }
        return transactionsService.getTransactionHistory(date, user, pageable);
    }

}
