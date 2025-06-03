package com.devbank.DevBank.ultilis;

import com.devbank.DevBank.entities.Transactions.Transactions;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class TransactionHashUtil {
    public String generateTransactionHash(Transactions t){
        String rawData = t.getSenderAccount().getUser().getId() + "|" +
                t.getReceiverAccount().getUser().getId() + "|" +
                t.getAmount() + "|" +
                t.getReciveKey() + "|" +
                t.getType() + "|" +
                t.getStatus() + "|" +
               t.getTimestamp();

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Erro a gerar hash de transação!");
        }
    }
}
