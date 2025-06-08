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
public class UserByKeyResponseDTO {
    private UUID userId;
    private String userName;
    private String userCpf;
    private UUID accountId;
    private String referenceKey;
}
