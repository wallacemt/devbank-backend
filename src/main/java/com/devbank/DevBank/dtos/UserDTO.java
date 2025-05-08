package com.devbank.DevBank.dtos;

import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    public UUID id;
    public String name;
    public String email;
    public String cpf;
    public UUID accountId;
    public Double balance;

    public UserDTO(User user, Account account) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.accountId = account.getUuid();
        this.balance = account.getBalance();
    }
}
