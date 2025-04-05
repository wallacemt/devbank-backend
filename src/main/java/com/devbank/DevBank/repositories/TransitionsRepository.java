package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.Transactions.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TransitionsRepository extends JpaRepository<Transactions, UUID> {
}
