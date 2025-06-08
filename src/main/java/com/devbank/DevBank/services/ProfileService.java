package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.UserProfileRequestDTO;
import com.devbank.DevBank.dtos.response.UserProfileResponseDTO;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.entities.UserAddress.UserAddress;
import com.devbank.DevBank.entities.UserProfile.UserProfile;
import com.devbank.DevBank.entities.UserTransactionPin.UserTransactionPin;
import com.devbank.DevBank.exeptions.PerfilAlreadyRegisteredException;
import com.devbank.DevBank.repositories.UserAddressRepository;
import com.devbank.DevBank.repositories.UserProfileRepository;
import com.devbank.DevBank.repositories.UserTransactionPinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserTransactionPinRepository userTransactionPinRepository;

    @Autowired
    @Qualifier("passwordEncoderV2")
    private PasswordEncoder passwordEncoderV2;

    public UserProfileResponseDTO   getUserProfile(User user) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUser(user);
        Optional<UserAddress> userAddressOpt = userAddressRepository.findByUser(user);

        if (userProfileOpt.isEmpty() || userAddressOpt.isEmpty()) {
            return null;
        }

        UserProfile userProfile = userProfileOpt.get();
        UserAddress userAddress = userAddressOpt.get();

        return new UserProfileResponseDTO(
                userAddress,
                userProfile.getSocialName(),
                userProfile.getBirthDate(),
                userProfile.getGender(),
                userProfile.getMaritalStatus(),
                userProfile.getIncome(),
                userProfile.getEmploymentStatus(),
                userProfile.getOccupation(),
                userProfile.getCompany(),
                userProfile.getEducation()
        );
    }


    public Map<String, String> createUserProfile(User user, UserProfileRequestDTO data) {
        if (userProfileRepository.findByUser(user).isPresent()) {
            throw new PerfilAlreadyRegisteredException("Perfil já cadastrado!");
        }
        UserProfile userProfile = new UserProfile(
                user,
                data.getSocialName(),
                data.getBirthDate(),
                data.getGender(),
                data.getMaritalStatus(),
                data.getIncome(),
                data.getEmploymentStatus(),
                data.getOccupation(),
                data.getCompany(),
                data.getEducation()
        );
        UserAddress userAddress = new UserAddress(
                user,
                data.getCep(),
                data.getStreet(),
                data.getNumber(),
                data.getComplement(),
                data.getCity(),
                data.getState()
        );

        if(data.getTransactionPin() == null) throw new RuntimeException("A senha de transação é obrigatória.");

        String encriptedTransactionPin = passwordEncoderV2.encode(data.getTransactionPin());

        UserTransactionPin userTransactionPin = new UserTransactionPin(
                user,
                encriptedTransactionPin
        );

        userProfileRepository.save(userProfile);
        userTransactionPinRepository.save(userTransactionPin);
        userAddressRepository.save(userAddress);

        return Map.of("message", "Perfil criado com sucesso!");
    }


}
