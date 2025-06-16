package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.ConfirmDepositResponseDTO;
import com.devbank.DevBank.dtos.response.CreateDepositResponseDTO;
import com.devbank.DevBank.dtos.response.DepositResponseDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Deposit.Deposit;
import com.devbank.DevBank.entities.Deposit.DepositStatus;
import com.devbank.DevBank.entities.Transactions.TransactionStatus;
import com.devbank.DevBank.entities.Transactions.TransactionType;
import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.DepositHasCompletedException;
import com.devbank.DevBank.repositories.AccountRepository;
import com.devbank.DevBank.repositories.DepositRepository;
import com.devbank.DevBank.repositories.TransitionsRepository;
import com.devbank.DevBank.ultilis.TransactionHashUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TransitionsRepository transitionsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionHashUtil transactionHashUtil;

    @Value("${spring.frontend.url}")
    private String frontendUrl;

    public CreateDepositResponseDTO createDeposit(User user, double amount) {
        Deposit deposit = new Deposit();

        deposit.setUser(user);
        deposit.setAmount(amount);
        deposit.setStatus(DepositStatus.PENDING);
        deposit.setCreatedAt(LocalDateTime.now());

        depositRepository.save(deposit);

        String qrCodeUrl = frontendUrl + "/deposit/" + deposit.getId();

        return new CreateDepositResponseDTO(deposit.getId(), qrCodeUrl);
    }


    public DepositResponseDTO getDepositById(UUID id, User user) {
        Deposit deposit = depositRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Deposito não encontrado!"));
        return new DepositResponseDTO(deposit.getId(), deposit.getStatus(), deposit.getAmount(), deposit.getCreatedAt());
    }

    public ConfirmDepositResponseDTO confirmDeposit(UUID depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new EntityNotFoundException("Depósito não encontrado"));

        if (deposit.getStatus() == DepositStatus.COMPLETED) {
            throw new DepositHasCompletedException("Depósito já processado");
        }

        User user = deposit.getUser();
        Account account = user.getAccount();
        account.setBalance(user.getAccount().getBalance() + deposit.getAmount());

        deposit.setStatus(DepositStatus.COMPLETED);

        depositRepository.save(deposit);
        accountRepository.save(account);
        Transactions transaction = new Transactions();
        transaction.setAmount(deposit.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setReciveKey(user.getCpf());
        transaction.setReceiverAccount(user.getAccount());
        transaction.setSenderAccount(user.getAccount());
        String hash = transactionHashUtil.generateTransactionHash(transaction);
        transaction.setHash(hash);

        transitionsRepository.save(transaction);

        return new ConfirmDepositResponseDTO("Depósito realizado com sucesso!", user.getAccount().getBalance());
    }
}
