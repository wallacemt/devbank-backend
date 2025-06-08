package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.TransactionsResponseDTO;
import com.devbank.DevBank.dtos.request.TransferPixRequestDTO;
import com.devbank.DevBank.dtos.response.UserByKeyResponseDTO;
import com.devbank.DevBank.dtos.request.UserKeyRequestDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Transactions.TransactionDirection;
import com.devbank.DevBank.entities.Transactions.TransactionStatus;
import com.devbank.DevBank.entities.Transactions.TransactionType;
import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserKeys.UserKeys;
import com.devbank.DevBank.entities.UserTransactionPin.UserTransactionPin;
import com.devbank.DevBank.exeptions.*;
import com.devbank.DevBank.repositories.*;
import com.devbank.DevBank.ultilis.TransactionHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private TransactionHashUtil transactionHashUtil;

    @Autowired
    @Qualifier("passwordEncoderV2")
    private PasswordEncoder passwordEncoderV2;

    @Autowired
    private ReceiptService recipientService;

    public UserByKeyResponseDTO getUserByKey(UserKeyRequestDTO key, User user) {
        Optional<UserKeys> userKeys = userKeysRepository.findByKeyValue(key.getUserKey());
        if (userKeys.isEmpty()) {
            throw new UserByKeyNotFound("Chave Pix não encontrada!");
        }

        Optional<User> userOpt = userRepository.findById(userKeys.get().getAccount().getUser().getId());
        Account accountUser = accountRepository.findByUser(user);

        if (accountUser.getUuid().equals(userKeys.get().getAccount().getUuid())) {
            return null;
        }
        return new UserByKeyResponseDTO(
                userOpt.get().getId(),
                userOpt.get().getName(),
                userOpt.get().getCpf(),
                userKeys.get().getAccount().getUuid(),
                key.getUserKey()
        );
    }

    @Transactional
    public Map<String, String> postTranferByPix(TransferPixRequestDTO pixRequestDTO, User user) {
        if (pixRequestDTO.getAmount() == null || pixRequestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }

        if (pixRequestDTO.getTransactionPin().isEmpty()) {
            throw new InvalidUserTransactionPin("Senha de transação inválida!");
        }

        UserTransactionPin userTransactionPin = userTransactionPinRepository.findByUser(user);

        if (!passwordEncoderV2.matches(pixRequestDTO.getTransactionPin(), userTransactionPin.getTransactionPin())) {
            throw new IncorrectUserTransactionPin("Senha de transação incorreta!");
        }

        Optional<UserKeys> userKeysOpt = userKeysRepository.findByKeyValue(pixRequestDTO.getReciveKey());
        if (userKeysOpt.isEmpty()) {
            throw new UserByKeyNotFound("Chave Pix não encontrada!");
        }

        Account senderAccount = accountRepository.findByUser(user);
        Account receiverAccount = userKeysOpt.get().getAccount();

        if (senderAccount.getUser().getId().equals(receiverAccount.getUser().getId())) {
            throw new InvalidSenderTransactionsException("Não é possível transferir para sua própria chave Pix.");
        }

        if (senderAccount.getBalance() < pixRequestDTO.getAmount()) {
            throw new InsuficientAmountException("Saldo insuficiente!");
        }

        senderAccount.setBalance(senderAccount.getBalance() - pixRequestDTO.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + pixRequestDTO.getAmount());

        Transactions transaction = new Transactions(
                senderAccount,
                receiverAccount,
                pixRequestDTO.getAmount(),
                TransactionStatus.COMPLETED,
                TransactionType.PIX,
                LocalDateTime.now(),
                pixRequestDTO.getReciveKey()
        );

        String hash = transactionHashUtil.generateTransactionHash(transaction);
        transaction.setHash(hash);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        transitionsRepository.save(transaction);

        Map<String, String> response = Map.of(
                "message", "Transferência realizada com sucesso!",
                "transactionId", transaction.getId().toString());

        return response;
    }


    public byte[] generateComprovante(UUID id) {
        Optional<Transactions> transaction = transitionsRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new TransactionNotFountExeception("Transação não encontrada!");
        }
        return recipientService.generateReceipt(transaction.get());
    }

    public Page<TransactionsResponseDTO> getTransactionHistory(
            LocalDate date,
            User user,
            Pageable pageable) {

        Account account = accountRepository.findByUser(user);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        return transitionsRepository
                .findByAccountAndDate(account, start, end, pageable)
                .map(tx -> {
                    boolean sent = tx.getSenderAccount().getUuid().equals(account.getUuid());
                    return new TransactionsResponseDTO(
                            tx.getId(),
                            tx.getAmount(),
                            tx.getStatus(),
                            sent ? tx.getReceiverAccount().getUser().getName() : tx.getSenderAccount().getUser().getName(),
                            tx.getTimestamp(),
                            tx.getReceiverAccount().getUser().getId(),
                            tx.getSenderAccount().getUser().getId(),
                            tx.getType(),
                            sent ? TransactionDirection.SENT : TransactionDirection.RECEIVED
                    );
                });
    }
}
