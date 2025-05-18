package com.devbank.DevBank.dtos;

import com.devbank.DevBank.entities.UserAddress.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
    public UserAddressResponseDTO address;
    public String socialName;
    public Date birthDate;
    public String gender;
    public String maritalStatus;
    public String income;
    public String employmentStatus;
    public String occupation;
    public String company;
    public String education;

    public UserProfileResponseDTO(UserAddress userAddress, String socialName, Date birthDate, String gender, String maritalStatus, String income, String employmentStatus, String occupation, String company, String education) {
        this.address = new UserAddressResponseDTO(userAddress.getCep(), userAddress.getStreet(), userAddress.getNumber(), userAddress.getComplement(), userAddress.getCity(), userAddress.getState());
        this.socialName = socialName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.income = income;
        this.employmentStatus = employmentStatus;
        this.occupation = occupation;
        this.company = company;
        this.education = education;
    }
}
