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
        String template = "";

        try {
            switch (type) {
                case WELCOME -> template = Files.readString(Path.of("src/main/resources/templates/welcome.html"));
                case VERIFICATION_CODE -> template = Files.readString(Path.of("src/main/resources/templates/verification_code.html"));
                case SUSPECT_ACTIVITY -> template = Files.readString(Path.of("src/main/resources/templates/suspect_activity.html"));
            }

            // Substituir variáveis no template (ex: ${nome} → Wallace)
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                template = template.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            return template;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o template de e-mail: " + e.getMessage());
        }
    }
}