package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
import com.devbank.DevBank.entities.UserTransactionPin.UserTransactionPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTransactionPinRepository extends JpaRepository<UserTransactionPin, UUID> {
    public UserTransactionPin findByUser(User user);
}
