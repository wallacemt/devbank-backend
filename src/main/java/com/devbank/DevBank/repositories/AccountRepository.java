package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    public Account findByUser(User user);
}
