package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserProfile.UserBonus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserBonusRepository extends JpaRepository<UserBonus, UUID> {
    public Optional<UserBonus> findByUser(User user);
}
