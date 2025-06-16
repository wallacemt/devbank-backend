package com.devbank.DevBank.entities.Transactions;

import com.devbank.DevBank.entities.Account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    @Min(0)
    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, unique = true, length = 100)
    private String hash;

    @Column(nullable = false)
    private String reciveKey;

    public Transactions(Account senderAccount, Account receiverAccount, Double amount, TransactionStatus status, TransactionType type, LocalDateTime timestamp, String reciveKey) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.timestamp = timestamp;
        this.reciveKey = reciveKey;
    }
}
