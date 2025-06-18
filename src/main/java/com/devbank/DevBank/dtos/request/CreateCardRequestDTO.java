package com.devbank.DevBank.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequestDTO {
    private String number;
    private String cardHoldName;
    private String cvv;
    private Double limiteApproved = 1000.0;
    private String expiryDate;
    private String cardTitle;
}
