package com.devbank.DevBank.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Email ou CPF obrigatório")
    private String emailOrCpf;

    @NotBlank(message = "Senha obrigatória")
    private String password;
}
