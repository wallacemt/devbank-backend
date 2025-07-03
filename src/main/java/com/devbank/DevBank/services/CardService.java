package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.request.CreateCardRequestDTO;
import com.devbank.DevBank.dtos.response.CardResponseDTO;
import com.devbank.DevBank.entities.Card.Card;
import com.devbank.DevBank.entities.User.User;
import com.devbank.DevBank.exeptions.DevCardHasCreatedException;
import com.devbank.DevBank.repositories.CardRepository;
import com.devbank.DevBank.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CvvEncryptionService cvvEncryptionService;

    @Autowired
    private AiService aiService;


    public double generatePreApprovedLimit(User user) {
        double limit = aiService.getSuggestedLimit(user);
        user.setPreApprovedLimit(limit);
        userRepository.save(user);
        return limit;
    }

    public CardResponseDTO createCardWithPreApprovedLimit(User user, String expiryDate) {
        List<Card> cards = cardRepository.findByUser(user);
        if (cards.stream().anyMatch(c -> c.getCardTitle().equals("DevCard"))) {
            throw new DevCardHasCreatedException("Cartão DevCard já criado");
        }
        double limit = user.getPreApprovedLimit() != null ? user.getPreApprovedLimit() : 1000.0;
        String cardNumber = aiService.generateFakeCardNumber();
        String cvv = aiService.generateFakeCvv();

        Card card = new Card();
        card.setUser(user);
        card.setCardHoldName(user.getName());
        LocalDate expiry = LocalDate.now().plusYears(5).plusMonths(4).plusDays(10);
        card.setExpiryDate(String.format("%02d/%d", expiry.getMonthValue(), expiry.getYear()));
        card.setDayExpiryFature(expiryDate);
        card.setCardNumber(cardNumber);
        card.setCardTitle("DevCard");
        card.setLimiteApproved(limit);
        card.setEncryptedCvv(cvvEncryptionService.encrypt(cvv));
        card.setCreatedAt(LocalDateTime.now());

        cardRepository.save(card);

        return toCardResponse(card);
    }

    public CardResponseDTO createManualCard(User user, CreateCardRequestDTO requestDTO) {
        Card card = new Card();
        card.setUser(user);
        card.setCardHoldName(requestDTO.getCardHoldName());
        card.setCardTitle(requestDTO.getCardTitle());
        card.setExpiryDate(requestDTO.getExpiryDate());
        card.setCardNumber(requestDTO.getNumber());
        card.setLimiteApproved(requestDTO.getLimiteApproved());
        card.setEncryptedCvv(cvvEncryptionService.encrypt(requestDTO.getCvv()));
        card.setCreatedAt(LocalDateTime.now());
        cardRepository.save(card);
        return toCardResponse(card);
    }

    public List<CardResponseDTO> getUserCards(User user) {
        return cardRepository.findByUser(user).stream()
                .map(this::toCardResponse)
                .toList();
    }


    public void deleteCard(UUID id, User user) {
        Card card = cardRepository.findById(id).filter(c -> c.getUser().getId().equals(user.getId())).orElseThrow(() -> new EntityNotFoundException("Cartão não encontrado"));
        cardRepository.delete(card);
    }

    public CardResponseDTO toCardResponse(Card card) {
        return new CardResponseDTO(card.getId(), card.getCardNumber(), card.getCardHoldName(), card.getExpiryDate(), card.getLimiteApproved(), card.getCardTitle());
    }
}
