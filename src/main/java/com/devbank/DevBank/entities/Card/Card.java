package com.devbank.DevBank.entities.Card;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String cardTitle;

    private String cardNumber;

    private String cardHoldName;

    private String expiryDate;

    private String dayExpiryFature;

    private String encryptedCvv;

    private double limiteApproved;

    private LocalDateTime createdAt;
}
