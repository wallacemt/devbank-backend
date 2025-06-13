package com.devbank.DevBank.repositories;

import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Transactions.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransitionsRepository extends JpaRepository<Transactions, UUID> {
    @Query("""
            SELECT t
              FROM Transactions t
             WHERE (t.senderAccount = :account
                 OR t.receiverAccount = :account)
               AND t.timestamp BETWEEN :start AND :end
             ORDER BY t.timestamp DESC
            """)
    Page<Transactions> findByAccountAndDate(
            @Param("account") Account account,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("""
                SELECT t FROM Transactions t 
                WHERE (t.senderAccount.user.id = :userId OR t.receiverAccount.user.id = :userId)
                AND t.timestamp >= :startDate
            """)
    List<Transactions> findLastSixMonthsTransactions(UUID userId, LocalDateTime startDate);

}
