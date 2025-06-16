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
public class CreateDepositResponseDTO {
    private UUID depositId;
    private String qrCodeUrl;
}
