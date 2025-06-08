package com.devbank.DevBank.dtos.response;

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
public class UserResoponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private AccountResponseDTO account;
    private UserProfileResponseDTO profile;

    public UserResoponseDTO(User user, Account account, UserProfileResponseDTO profile) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.account = new AccountResponseDTO(account.getUuid(), account.getBalance(), account.getCreatedAt());
        this.profile = profile;
    }

    public UserResoponseDTO(User user, Account account) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.account = new AccountResponseDTO(account.getUuid(), account.getBalance(), account.getCreatedAt());

    }
}
