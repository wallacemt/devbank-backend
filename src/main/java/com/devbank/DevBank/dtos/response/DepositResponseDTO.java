package com.devbank.DevBank.dtos.response;

import com.devbank.DevBank.entities.Deposit.DepositStatus;
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
public class DepositResponseDTO {
    private UUID id;
    private DepositStatus depositStatus;
    private double value;
    private LocalDateTime createdAt;
}
