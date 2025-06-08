package com.devbank.DevBank.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDTO {
    @NotBlank(message = "Digite o CEP")
    public String cep;

    @NotBlank(message = "Digite seu endereço")
    public String street;

    @NotBlank(message = "Digite o numero")
    public String number;

    public String complement;

    @NotBlank(message = "Digite A cidade")
    public String city;

    @NotBlank(message = "Digite o estado")
    public String state;

    public String socialName;
    @NotBlank(message = "Digite a data de nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date birthDate;

    @NotBlank(message = "Digite seu Genero")
    public String gender;

    @NotBlank(message = "Digite Seu Estado Civil")
    public String maritalStatus;

    @NotBlank(message = "Digite o rendimento")
    public String income;

    @NotBlank(message = "Digite o status de emprego")
    public String employmentStatus;

    @NotBlank(message = "Digite a ocupação")
    public String occupation;

    @NotBlank(message = "Digite a empresa")
    public String company;

    @NotBlank(message = "Digite a escolaridade")
    public String education;

    @NotBlank(message = "Digite o PIN de transação")
    public String transactionPin;
}
