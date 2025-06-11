package com.devbank.DevBank.dtos.response;
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
public class StashResponseDTO {
    private UUID id;
    private String name;
    private Double value;
    private String description;
    private Double goal;
    private LocalDateTime createdAt;
    private LocalDateTime lastMovimentation;
}
