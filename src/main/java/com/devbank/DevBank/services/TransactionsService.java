package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.UserByKeyResponseDTO;
import com.devbank.DevBank.dtos.UserKeyRequestDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Transactions.TransactionStatus;
import com.devbank.DevBank.entities.Transactions.TransactionType;
import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserKeys.UserKeys;
import com.devbank.DevBank.entities.UserTransactionPin.UserTransactionPin;
import com.devbank.DevBank.exeptions.*;
import com.devbank.DevBank.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class TransactionsService {

    @Autowired
    private TransitionsRepository transitionsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserKeysRepository userKeysRepository;

    @Autowired
    private UserTransactionPinRepository userTransactionPinRepository;


    @Autowired
    @Qualifier("passwordEncoderV2")
    private PasswordEncoder passwordEncoderV2;


    public UserByKeyResponseDTO getUserByKey(UserKeyRequestDTO key) {
        Optional<UserKeys> userKeys = userKeysRepository.findByKeyValue(key.getUserKey());
        if (userKeys.isEmpty()) {
            throw new UserByKeyNotFound("Chave Pix não encontrada!");
        }
        Optional<User> userOpt = userRepository.findById(userKeys.get().getAccount().getUser().getId());
        return new UserByKeyResponseDTO(
                userOpt.get().getId(),
                userOpt.get().getName(),
                userOpt.get().getCpf(),
                userKeys.get().getAccount().getUuid()
        );
    }

    @Transactional
    public Map<String, String> postTranferByPix(Double amount, String reciveKey, User user, String transactionPin) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        if (transactionPin.isEmpty()) {
            throw new InvalidUserTransactionPin("Senha de transação inválida!");
        }

        UserTransactionPin userTransactionPin = userTransactionPinRepository.findByUser(user);

        if (!passwordEncoderV2.matches(transactionPin, userTransactionPin.getTransactionPin())) {
            throw new IncorrectUserTransactionPin("Senha de transação incorreta!");
        }

        Optional<UserKeys> userKeysOpt = userKeysRepository.findByKeyValue(reciveKey);
        if (userKeysOpt.isEmpty()) {
            throw new UserByKeyNotFound("Chave Pix não encontrada!");
        }

        Account senderAccount = accountRepository.findByUser(user);
        Account receiverAccount = userKeysOpt.get().getAccount();

        if (senderAccount.getUser().getId().equals(receiverAccount.getUser().getId())) {
            throw new InvalidSenderTransactionsException("Não é possível transferir para sua própria chave Pix.");
        }

        if (senderAccount.getBalance() < amount) {
            throw new InsuficientAmountException("Saldo insuficiente!");
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        Transactions transaction = new Transactions(
                senderAccount,
                receiverAccount,
                amount,
                TransactionStatus.COMPLETED,
                TransactionType.PIX
        );

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        transitionsRepository.save(transaction);

        return Map.of("message", "Transferência realizada com sucesso!");
    }


}
