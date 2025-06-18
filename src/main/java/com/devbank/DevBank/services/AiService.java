package com.devbank.DevBank.services;

import com.devbank.DevBank.entities.User.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AiService {

    @Value("${api.gemini.token}")
    private String API_KEY;

    private final WebClient webClient;
    private final Random random;

    public AiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
        this.random = new Random();
    }

    public double getSuggestedLimit(User user) {
        String prompt = String.format(
                "Analise o seguinte perfil de usuário e sugira um limite de crédito em um número apenas no formato json {sugestedLimit: valor}:" +
                        "\nSalário: %s" +
                        "\nOcupação: %s" +
                        "\nEstado Civil: %s" +
                        "\nEmpresa onde trabalha: %s" +
                        "\nEducação: %s" +
                        "\nRegime de trabalho: %s" +
                        "\nAniversário: %s",
                user.getProfile().getIncome(),
                user.getProfile().getOccupation(),
                user.getProfile().getMaritalStatus(),
                user.getProfile().getCompany(),
                user.getProfile().getEducation(),
                user.getProfile().getEmploymentStatus(),
                user.getProfile().getBirthDate()
        );

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of(
                                "text", prompt
                        ))
                ))
        );

        try {
            Map response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-2.0-flash:generateContent")
                            .queryParam("key", API_KEY)
                            .build()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String textResult = extractTextFromGeminiResponse(response);
            System.out.println("Resposta da IA: " + textResult);

            try {
                return Double.parseDouble(textResult.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter o valor retornado. Retornando valor padrão 1000.");
                return 1000.0;
            }

        } catch (Exception e) {
            System.out.println("Erro ao chamar Gemini: " + e.getMessage());
            return 1000.0;  // Fallback
        }
    }

    private String extractTextFromGeminiResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao fazer parse da resposta da Gemini: " + e.getMessage());
        }
        return "1000";
    }

    public String generateFakeCardNumber() {
        StringBuilder cardNumber = new StringBuilder("4");
        for (int i = 0; i < 14; i++) {
            cardNumber.append(random.nextInt(10));
        }
        int checkDigit = getLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);
        return cardNumber.toString();
    }

    public String generateFakeCvv() {
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }

    private int getLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}
