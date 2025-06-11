package com.devbank.DevBank.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StashRequestDTO {
    @NotBlank(message = "Nome Obrigatório!")
    private String name;

    @NotNull(message = "Valor Obrigatório!")
    @Min(0)
    private Double value;


    private String description;

    @Min(0)
    private Double goal;

}
