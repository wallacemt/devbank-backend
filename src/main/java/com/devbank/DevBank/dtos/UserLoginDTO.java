package com.devbank.DevBank.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email Inválido")
    public String email;
    
    @NotBlank(message = "Senha é obrigatório")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    public String password;
}