package com.devbank.DevBank.entities.UserProfile;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String socialName;

    private Date birthDate;

    private String gender;

    private String maritalStatus;

    private String income;

    private String employmentStatus;

    private String occupation;

    private String company;

    private String education;

    public UserProfile(User user, String socialName, Date birthDate, String gender, String maritalStatus, String income, String employmentStatus, String occupation, String company, String education) {
        this.user = user;
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
