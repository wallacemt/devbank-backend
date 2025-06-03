package com.devbank.DevBank.dtos;

import com.devbank.DevBank.entities.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferPixRequestDTO {
    private Double amount;
    private String reciveKey;
    private String transactionPin;
}
