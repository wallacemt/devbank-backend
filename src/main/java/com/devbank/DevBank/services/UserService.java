package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.UserResoponseDTO;
import com.devbank.DevBank.dtos.response.UserProfileResponseDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserProfile.UserBonus;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
import com.devbank.DevBank.exeptions.BonusHasReclaimException;
import com.devbank.DevBank.exeptions.IncompleteProfileException;
import com.devbank.DevBank.repositories.AccountRepository;
import com.devbank.DevBank.repositories.UserBonusRepository;
import com.devbank.DevBank.repositories.UserProfileRepository;
import com.devbank.DevBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserBonusRepository userBonusRepository;

    @Autowired
    @Qualifier("passwordEncoderV2")
    private PasswordEncoder passwordEncoderV2;

    public UserResoponseDTO getUser(User user) {
        Optional<User> userOpt = userRepository.findById(user.getId());
        Account account = accountRepository.findByUser(user);

        if (userOpt.isPresent() && account != null) {
            UserProfileResponseDTO profile = profileService.getUserProfile(user);
            return (profile != null)
                    ? new UserResoponseDTO(userOpt.get(), account, profile)
                    : new UserResoponseDTO(userOpt.get(), account);
        }

        return null;
    }


    public Map<String, String> reclaimBonus(User user) {
        Optional<UserBonus> userBonusOpt = userBonusRepository.findByUser(user);
        Optional<UserProfile> userProfile = userProfileRepository.findByUser(user);
        Account account = accountRepository.findByUser(user);

        if (userBonusOpt.isPresent()) {
            throw new BonusHasReclaimException("Bonus já resgatado!");
        } else if (userProfile.isEmpty()) {
            throw new IncompleteProfileException("Perfil Incompleto! condição para resgatar não atendida!");
        }

        UserBonus userBonus = new UserBonus(user, 250.00);
        account.setBalance(account.getBalance() + 250.00);
        userBonusRepository.save(userBonus);
        accountRepository.save(account);

        return  Map.of("message", "Bonus resgatado com sucesso!");
    }

}
