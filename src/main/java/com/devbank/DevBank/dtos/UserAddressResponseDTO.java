package com.devbank.DevBank.dtos;

import com.devbank.DevBank.entities.UserAddress.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class UserAddressResponseDTO {
    public String cep;
    public String street;
    public String number;
    public String complement;
    public String city;
    public String state;

    public UserAddressResponseDTO(UserAddress userAddress) {
        this.cep = userAddress.getCep();
        this.street = userAddress.getStreet();
        this.number = userAddress.getNumber();
        this.complement = userAddress.getComplement();
        this.city = userAddress.getCity();
        this.state = userAddress.getState();
    }
}