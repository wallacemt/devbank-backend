package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.LoginDTO;
import com.devbank.DevBank.dtos.UserDTO;
import com.devbank.DevBank.dtos.UserProfileResponseDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
import com.devbank.DevBank.repositories.AccountRepository;
import com.devbank.DevBank.repositories.UserProfileRepository;
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

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ProfileService profileService;

    public UserDTO getUser(User user) {
        Optional<User> userOpt = userRepository.findById(user.getId());
        Account account = accountRepository.findByUser(user);

        if (userOpt.isPresent() && account != null) {
            UserProfileResponseDTO profile = profileService.getUserProfile(user);
            return (profile != null)
                    ? new UserDTO(userOpt.get(), account, profile)
                    : new UserDTO(userOpt.get(), account);
        }

        return null;
    }


}
