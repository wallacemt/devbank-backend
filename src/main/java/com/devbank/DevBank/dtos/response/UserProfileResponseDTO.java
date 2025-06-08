package com.devbank.DevBank.dtos.response;

import com.devbank.DevBank.entities.UserAddress.UserAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private UserAddressResponseDTO address;
    private String socialName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String gender;
    private String maritalStatus;
    private String income;
    private String employmentStatus;
    private String occupation;
    private String company;
    private String education;

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
