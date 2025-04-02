package com.devbank.DevBank.entities.UserKeys;

import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.UserKeys.UserKeyType;
import com.devbank.DevBank.entities.UserKeys.UserKeyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKeys {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserKeyType keyType;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String keyValue;
}
