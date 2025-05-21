package com.devbank.DevBank.entities.UserAddress;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @NotBlank
    private String cep;
    @NotBlank
    private String street;
    @NotBlank
    private String number;

    private String complement;

    @NotBlank
    private String city;
    @NotBlank
    private String state;

    public UserAddress(User user, String cep, String street, String number, String complement, String city, String state) {
        this.user = user;
        this.cep = cep;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.city = city;
        this.state = state;
    }
}
