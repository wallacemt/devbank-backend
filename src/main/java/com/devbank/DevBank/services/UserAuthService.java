package com.devbank.DevBank.services;

import com.devbank.DevBank.config.security.TokenService;
import com.devbank.DevBank.dtos.request.EmailOrCpfVerificationDTO;
import com.devbank.DevBank.dtos.request.LoginDTO;
import com.devbank.DevBank.dtos.request.LoginVerifyDTO;
import com.devbank.DevBank.dtos.request.UserRegisterDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Stash.Stash;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserBlocked.UserBlocked;
import com.devbank.DevBank.entities.UserKeys.UserKeyType;
import com.devbank.DevBank.entities.UserKeys.UserKeys;
import com.devbank.DevBank.exeptions.*;
import com.devbank.DevBank.repositories.*;
import com.devbank.DevBank.ultilis.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StashRepository stashRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserKeysRepository userKeysRepository;
    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserBlockedRepository userBlockedRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerifyService emailVerifyService;

    private final Map<String, Integer> tentativas = new HashMap<>();

    public Map<String, String> userRegister(UserRegisterDTO data) {
        if (userRepository.findByEmail(data.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("Emaill já resgistrado!");
        }

        if (userRepository.findByCpf(data.getCpf()).isPresent()) {
            throw new CpfAlreadyRegisteredException("Cpf já registrado!");
        }
        if (!data.getPassword().equals(data.getPasswordConfirmation())) {
            throw new PasswordsDoNotMatchException("As senhas não coincidem!");
        }


        String encriptedPassword = passwordEncoder.encode(data.getPassword());

        User newUser = new User(
                data.getName(),
                data.getEmail(),
                data.getCpf(),
                encriptedPassword
        );

        Account newUserAccount = new Account(newUser);

        Map<String, String> variables = new HashMap<>();
        variables.put("nome", newUser.getName());

        Stash stash = new Stash();
        stash.setStashName("Saldo de Emergência");
        stash.setUser(newUser);
        stash.setDescription("Guarde Saldos de emergência aqui.");

        stashRepository.save(stash);

        emailService.enviarEmailHtml(
                newUser.getEmail(),
                "Bem-vindo ao DevBank 🚀",
                EmailType.WELCOME,
                variables
        );
        userRepository.save(newUser);
        accountRepository.save(newUserAccount);


        UserKeys userKeysCpf = new UserKeys(newUserAccount, UserKeyType.CPF, newUser.getCpf());
        UserKeys userKeysMail = new UserKeys(newUserAccount, UserKeyType.EMAIL, newUser.getEmail());
        userKeysRepository.save(userKeysCpf);
        userKeysRepository.save(userKeysMail);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso!");
        return response;
    }


    public Map<String, String> loginUser(LoginDTO data, String ipAddress, boolean twoFa) {
        User user = userRepository.findByEmailOrCpf(data.getEmailOrCpf(), data.getEmailOrCpf())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado, Email Ou Cpf Incorreto!"));

        Optional<UserBlocked> ipBlockedOpt = userBlockedRepository.findByIpAddress(ipAddress);
        if (ipBlockedOpt.isPresent()) {
            UserBlocked blocked = ipBlockedOpt.get();
            if (blocked.getUnlockedTime().isAfter(LocalDateTime.now())) {
                Duration dur = Duration.between(LocalDateTime.now(), blocked.getUnlockedTime());
                throw new AccountBlockedException("IP bloqueado. Tente novamente em " + dur.toMinutes() + " minutos.");
            } else {
                userBlockedRepository.delete(blocked);
            }
        }

        Optional<UserBlocked> userBlockedOpt = userBlockedRepository.findByUser(user);
        if (userBlockedOpt.isPresent()) {
            UserBlocked blocked = userBlockedOpt.get();
            if (blocked.getUnlockedTime().isAfter(LocalDateTime.now())) {
                Duration dur = Duration.between(LocalDateTime.now(), blocked.getUnlockedTime());
                throw new AccountBlockedException("Conta bloqueada. Tente novamente em " + dur.toMinutes() + " minutos.");
            } else {
                userBlockedRepository.delete(blocked);
                tentativas.remove(user.getEmail());
            }
        }
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            int falhas = tentativas.getOrDefault(user.getEmail(), 0) + 1;
            tentativas.put(user.getEmail(), falhas);

            if (falhas >= 3) {
                UserBlocked userBlocked = new UserBlocked();
                userBlocked.setUser(user);
                userBlocked.setIpAddress(ipAddress);
                userBlocked.setUnlockedTime(LocalDateTime.now().plusHours(1));
                userBlockedRepository.save(userBlocked);

                tentativas.remove(user.getEmail());
                Map<String, String> variables = new HashMap<>();
                variables.put("nome", user.getName());
                emailService.enviarEmailHtml(
                        user.getEmail(),
                        "Atividades Suspeitas Na sua Conta ⚠️",
                        EmailType.SUSPECT_ACTIVITY,
                        variables
                );
                throw new AccountBlockedException("Conta bloqueada por tentativas mal sucedidas.");
            }

            throw new IncorrectPasswordException("Senha incorreta. " + (3 - falhas) + " tentativa(s) restante(s).");
        }

        tentativas.remove(user.getEmail());

        if (!twoFa) {
            String tokenJwt = tokenService.generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", tokenJwt);
            String[] names = user.getName().split(" ");
            String firstName = names[0];
            String lastName = names[names.length - 1];
            response.put("message", "Bem-vindo(a), " + firstName + " " + lastName + "!");
            return response;
        }

        emailVerifyService.generate2FACode(user);
        return Map.of("message", "Código de verificação enviado", "email", user.getEmail());
    }


    public Map<String, String> verifyCodeAndFinishLogin(LoginVerifyDTO data) {
        User user = userRepository.findByEmailOrCpf(data.getEmailOrCpf(), data.getEmailOrCpf())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        emailVerifyService.validCode(user.getEmail(), data.getToken());
        String tokenJwt = tokenService.generateToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", tokenJwt);
        String[] names = user.getName().split(" ");
        String firstName = names[0];
        String lastName = names[names.length - 1];
        response.put("message", "Bem-vindo(a), " + firstName + " " + lastName + "!");
        return response;
    }

    public Map<String, String> resend2FACode(String credential) {
        User user = userRepository.findByEmailOrCpf(credential, credential)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        emailVerifyService.invalidateCode(user.getEmail());

        return emailVerifyService.generate2FACode(user);
    }


    public boolean EmailOrCpfVerifications(EmailOrCpfVerificationDTO data) {
        Optional<User> user = userRepository.findByEmailOrCpf(data.getEmailOrCpf(), data.getEmailOrCpf());
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public boolean verifyEmailOrCpf(String data) {
        Optional<User> user = userRepository.findByEmailOrCpf(data, data);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }
}
