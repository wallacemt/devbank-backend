package com.devbank.DevBank.entities.UserAddress;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
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


    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String street;
    private String number;
    private String complement;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
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
