package com.devbank.DevBank.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CvvEncryptionService {
    @Value("${api.security.token.secret}")
    private String secret;


    public String encrypt(String cvv) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(secret.substring(0, 16).getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(cvv.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao encriptar CVV: ", e);
        }
    }

    public String decrypt(String encryptedCvv) {
        try {

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedCvv));
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decrypt CVV: ", e);
        }
    }

}
