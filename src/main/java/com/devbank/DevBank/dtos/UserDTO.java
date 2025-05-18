package com.devbank.DevBank.dtos;

import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
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
    public AccountResponseDTO account;
    public UserProfileResponseDTO profile;

    public UserDTO(User user, Account account, UserProfileResponseDTO profile) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.account = new AccountResponseDTO(account.getUuid(), account.getBalance(), account.getCreatedAt());
        this.profile = profile;
    }

    public UserDTO(User user, Account account) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.account = new AccountResponseDTO(account.getUuid(), account.getBalance(), account.getCreatedAt());

    }
}
