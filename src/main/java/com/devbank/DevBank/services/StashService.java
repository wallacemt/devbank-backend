package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.request.StashRequestDTO;
import com.devbank.DevBank.dtos.response.StashResponseDTO;
import com.devbank.DevBank.entities.Account.Account;
import com.devbank.DevBank.entities.Stash.Stash;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.InsuficientAmountException;
import com.devbank.DevBank.repositories.AccountRepository;
import com.devbank.DevBank.repositories.StashRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StashService {
    @Autowired
    private StashRepository stashRepository;

    @Autowired
    private AccountRepository accountRepository;

    public StashResponseDTO create(StashRequestDTO data, User user) {
        if (user.getAccount().getBalance() < data.getValue()) {
            throw new InsuficientAmountException("Limite de saldo excedido!");
        }

        Account account = user.getAccount();
        account.setBalance((user.getAccount().getBalance() - data.getValue()));
        accountRepository.saveAndFlush(account);
        Stash stash = Stash.builder()
                .stashName(data.getName())
                .stashValue(data.getValue())
                .description(data.getDescription())
                .user(user)
                .goal(data.getGoal())
                .build();
        return toDTO(stashRepository.save(stash));
    }


    public Page<StashResponseDTO> findAll(User user, Pageable pageable) {
        return stashRepository.findByUser(user, pageable).map(this::toDTO);
    }

    public StashResponseDTO findById(UUID id, User user) {
        Stash stash = stashRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Stash não encontrada!"));
        return toDTO(stash);
    }

    public StashResponseDTO addAmount(UUID id, Double value, User user) {
        if (user.getAccount().getBalance() < value) {
            throw new InsuficientAmountException("Limite de saldo excedido!");
        }
        Stash stash = stashRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Stash não encontrada!"));
        stash.setStashValue(stash.getStashValue() + value);
        stash.setLastMovimentation(LocalDateTime.now());
        Account account = user.getAccount();

        account.setBalance(account.getBalance() - value);
        accountRepository.saveAndFlush(account);
        return toDTO(stashRepository.save(stash));

    }

    public StashResponseDTO getAmount(UUID id, User user, Double value) {
        Stash stash = stashRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Stash não encontrada!"));

        if (stash.getStashValue() < value) {
            throw new InsuficientAmountException("Limite de saldo excedido!");
        }

        Account account = user.getAccount();
        account.setBalance(account.getBalance() + value);
        stash.setStashValue(stash.getStashValue() - value);
        stash.setLastMovimentation(LocalDateTime.now());

        accountRepository.saveAndFlush(account);
        stashRepository.saveAndFlush(stash);

        return toDTO(stash);
    }

    public StashResponseDTO update(UUID id, StashRequestDTO data, User user) {
        Stash stash = stashRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Stash não encontrada!"));
        stash.setStashName(data.getName());
        stash.setDescription(data.getDescription());
        stash.setGoal(data.getGoal());
        return toDTO(stashRepository.save(stash));
    }

    public String delete(UUID id, User user) {
        Stash stash = stashRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Stash não encontrada!"));

        Account account = user.getAccount();
        account.setBalance(user.getAccount().getBalance() + stash.getStashValue());
        accountRepository.saveAndFlush(account);

        stashRepository.delete(stash);
        return "Stash removida com sucesso!";
    }

    private StashResponseDTO toDTO(Stash stash) {
        return new StashResponseDTO(
                stash.getId(),
                stash.getStashName(),
                stash.getStashValue(),
                stash.getDescription(),
                stash.getGoal(),
                stash.getCreatedAt(),
                stash.getLastMovimentation()
        );
    }
}
