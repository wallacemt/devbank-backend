package com.devbank.DevBank.dtos.response;

import com.devbank.DevBank.entities.UserAddress.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class UserAddressResponseDTO {
    private String cep;
    private String street;
    private String number;
    private String complement;
    private String city;
    private String state;

    public UserAddressResponseDTO(UserAddress userAddress) {
        this.cep = userAddress.getCep();
        this.street = userAddress.getStreet();
        this.number = userAddress.getNumber();
        this.complement = userAddress.getComplement();
        this.city = userAddress.getCity();
        this.state = userAddress.getState();
    }
}