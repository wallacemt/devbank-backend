package com.devbank.DevBank.entities.UserBlocked;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_blocked")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBlocked {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private LocalDateTime unlockedTime;
}
