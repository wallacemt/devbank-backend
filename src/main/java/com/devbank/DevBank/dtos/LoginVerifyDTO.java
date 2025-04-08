package com.devbank.DevBank.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginVerifyDTO {

    @NotBlank(message = "Email ou CPF obrigatório")
    public String emailOrCpf;

    @NotBlank(message = "Token obrigatório")
    public String token;
}
