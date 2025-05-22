package com.devbank.DevBank.entities.UserTransactionPin;


import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_transaction_pin")
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionPin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "A senha de transação é obrigatória.")
    private String transactionPin;

    public UserTransactionPin(User user, String transactionPin) {
        this.user = user;
        this.transactionPin = transactionPin;
    }
}
