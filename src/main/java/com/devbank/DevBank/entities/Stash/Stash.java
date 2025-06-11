package com.devbank.DevBank.entities.Stash;

import com.devbank.DevBank.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "stash")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stash {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String stashName;

    @Column(nullable = false)
    private Double stashValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String description;

    private Double goal;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastMovimentation;
}
