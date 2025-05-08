package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.LoginDTO;
import com.devbank.DevBank.dtos.UserDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.repositories.AccountRepository;
import com.devbank.DevBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public UserDTO getUser(User user) {
        Optional<User> userOpt = userRepository.findById(user.getId());
        Account accountOpt = accountRepository.findByUser(user);

        if (userOpt.isPresent() && accountOpt != null) {
            return new UserDTO(userOpt.get(), accountOpt);
        } else {
            return null;
        }
    }

}
