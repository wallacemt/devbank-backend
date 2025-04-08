package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.VerifyUser.VerifyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerifyUserRepository extends JpaRepository<VerifyUser, UUID> {
    Optional<VerifyUser> findFirstByUserAndTokenAndIsValidTrueOrderByCreatedAtDesc(User user, String token);

    Optional<VerifyUser> findFirstByUserAndIsValidTrueOrderByCreatedAtDesc(User user);

    @Transactional
    @Modifying
    @Query("UPDATE VerifyUser v SET v.isValid = false WHERE v.user.id = :userId")
    void invalidateAllByUserId(UUID userId);
}
