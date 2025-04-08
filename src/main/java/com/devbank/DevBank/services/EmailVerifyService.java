package com.devbank.DevBank.services;

import com.devbank.DevBank.exeptions.InvalidCodeException;
import com.devbank.DevBank.ultilis.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailVerifyService {
    private  final Map<String, EmailCodeData> codeStore = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserAuthService userAuthService;

    public Map<String, String> generateAndStoreCode(String email, String name){
        if(userAuthService.verifyEmailOrCpf(email)){
            throw new DuplicateKeyException("Email já em uso!");
        }
        invalidateCode(email);
        String code = userAuthService.generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        codeStore.put(email, new EmailCodeData(code, expiresAt));

        Map<String, String> variables = new HashMap<>();
        variables.put("nome", name);
        variables.put("codigo", code);
        emailService.enviarEmailHtml(
                email,
                "Email Verify 🔏",
                EmailType.VERIFICATION_CODE,
                variables
        );
        return Map.of("message", "Código de verificação enviado");
    }

    public void validCode(String email, String inputCode){
        EmailCodeData data = codeStore.get(email);

        if(data == null){
            throw new InvalidCodeException("Código de verificação expirado ou inválido");
        }
        if(data.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidCodeException("Código de verificação expirado");
        }

        if(!data.getCode().equals(inputCode)){
            throw new InvalidCodeException("Código de verificação inválido");
        }

        invalidateCode(email);
    }

    public void invalidateCode(String email){
        codeStore.remove(email);
    }


    private static class EmailCodeData {
        private final String code;
        private final LocalDateTime expiresAt;

        public EmailCodeData(String code, LocalDateTime expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }
    }
}
