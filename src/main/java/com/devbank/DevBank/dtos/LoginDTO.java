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
public class LoginDTO {

    @NotBlank(message = "Email ou CPF   obrigatorio")
    private String emailOrCpf;

    @NotBlank(message = "Senha   obrigatorio")
    private String password;
}
