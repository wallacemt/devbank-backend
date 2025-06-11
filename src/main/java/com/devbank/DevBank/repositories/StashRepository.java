package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.Stash.Stash;
import com.devbank.DevBank.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StashRepository extends JpaRepository<Stash, UUID> {
    List<Stash> findByUser(User user);
}
