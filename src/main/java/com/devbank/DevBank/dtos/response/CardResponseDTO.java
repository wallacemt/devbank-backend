package com.devbank.DevBank.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO {
    private UUID id;
    private String number;
    private String cardHolderName;
    private String expiryDate;
    private double limitApproved;
    private String cardTitle;
}
