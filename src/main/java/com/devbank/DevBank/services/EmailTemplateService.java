package com.devbank.DevBank.services;

import com.devbank.DevBank.ultilis.EmailType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class EmailTemplateService {

     public String getTemplate(EmailType type, Map<String, String> variables) {
        String templatePath = switch (type) {
            case WELCOME -> "templates/welcome.html";
            case VERIFICATION_CODE -> "templates/verification_code.html";
            case SUSPECT_ACTIVITY -> "templates/suspect_activity.html";
        };

        try (InputStream inputStream = new ClassPathResource(templatePath).getInputStream()) {
            String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                template = template.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            return template;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o template de e-mail: " + e.getMessage());
        }
    }
}
