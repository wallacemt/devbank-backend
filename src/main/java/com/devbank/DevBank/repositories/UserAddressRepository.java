package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserAddress.UserAddress;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    public Optional<UserAddress> findByUser(User user);
}
