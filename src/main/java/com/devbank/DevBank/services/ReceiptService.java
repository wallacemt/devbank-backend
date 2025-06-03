package com.devbank.DevBank.services;

import com.devbank.DevBank.entities.Transactions.Transactions;
import com.devbank.DevBank.ultilis.EmailType;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptService {
    @Autowired
    private EmailTemplateService emailTemplateService;

    public byte[] generateReceipt(Transactions transaction) {
        Map<String, String> vars = new HashMap<>();
        vars.put("id", String.valueOf(transaction.getId()));
        vars.put("type", transaction.getType().toString());
        vars.put("data", transaction.getTimestamp()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        vars.put("valor", String.format("%.2f", transaction.getAmount()));
        vars.put("status", transaction.getStatus().toString());
        vars.put("remetente_nome", transaction.getSenderAccount().getUser().getName());
        vars.put("remetente_cpf", transaction.getSenderAccount().getUser().getCpf());
        vars.put("destinatario_nome", transaction.getReceiverAccount().getUser().getName());
        vars.put("destinatario_cpf", transaction.getReceiverAccount().getUser().getCpf());
        vars.put("chave_pix", transaction.getReciveKey());
        vars.put("transaction_hash", transaction.getHash());
        vars.put("remetente_conta", transaction.getSenderAccount().getUuid().toString());
        vars.put("destinatario_conta", transaction.getReceiverAccount().getUuid().toString());

        String htmlTemp = emailTemplateService.getTemplate(EmailType.RECEIPT, vars);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlTemp, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar comprovante", e);

        }
    }
}
