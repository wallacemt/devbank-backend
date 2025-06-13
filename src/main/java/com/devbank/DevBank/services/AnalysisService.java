package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.FinancialAnalysisDTO;
import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.repositories.TransitionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;


@Service
public class AnalysisService {

    @Autowired
    private TransitionsRepository transitionsRepository;

    public List<FinancialAnalysisDTO> getLastSixMonthsAnalysis(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMonths(5).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Transactions> transactions = transitionsRepository.findLastSixMonthsTransactions(userId, start);

        Map<YearMonth, double[]> resumo = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            YearMonth ym = YearMonth.from(now.minusMonths(i));
            resumo.put(ym, new double[]{0.0, 0.0}); // [entradas, saidas]
        }

        for (Transactions tx : transactions) {
            YearMonth ym = YearMonth.from(tx.getTimestamp());
            double[] valores = resumo.getOrDefault(ym, new double[]{0.0, 0.0});

            if (tx.getReceiverAccount().getUser().getId().equals(userId)) {
                valores[0] += tx.getAmount();
            } else if (tx.getSenderAccount().getUser().getId().equals(userId)) {
                valores[1] += tx.getAmount();
            }

            resumo.put(ym, valores);
        }

        return resumo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    String mes = entry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                    double entradas = entry.getValue()[0];
                    double saidas = entry.getValue()[1];
                    return new FinancialAnalysisDTO(mes, entradas, saidas);
                })
                .toList();
    }
}
