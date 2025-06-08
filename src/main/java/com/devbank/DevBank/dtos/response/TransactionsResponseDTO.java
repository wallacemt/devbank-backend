package com.devbank.DevBank.dtos.response;

import com.devbank.DevBank.entities.Transactions.TransactionDirection;
import com.devbank.DevBank.entities.Transactions.TransactionStatus;
import com.devbank.DevBank.entities.Transactions.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponseDTO {
    private UUID id;
    private double amount;
    private TransactionStatus status;
    private String outherAccountUsername;
    private LocalDateTime timestamp;
    private UUID receiverId;
    private UUID senderId;
    private TransactionType transactionType;
    private TransactionDirection direction;
}
