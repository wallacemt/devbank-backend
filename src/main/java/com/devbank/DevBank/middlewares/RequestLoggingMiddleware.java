package com.devbank.DevBank.middlewares;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;


@Component
public class RequestLoggingMiddleware extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingMiddleware.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }

        Instant start = Instant.now();

        logger.info("========== [Nova Requisição] ==========");
        logger.info("🔹 Método: {}", request.getMethod());
        logger.info("🔹 URL: {}", request.getRequestURI());
        logger.info("🔹 Data/Hora: {}", LocalDateTime.now());
        logger.info("🔹 IP: {}", request.getRemoteAddr());

        CachedBodyHttpServetResponse responseWrapper = new CachedBodyHttpServetResponse(response);
        filterChain.doFilter(request, responseWrapper);

        String responseBody = responseWrapper.getBody();
        if (responseBody.length() < 1000) {
            logger.info("📌 Corpo capturado: {}", responseBody);
        } else {
            logger.info("📌 Corpo capturado: [corpo omitido: tamanho excede 1KB]");
        }


        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        logger.info("========== [Resposta Enviada] ==========");
        logger.info("✅ Status: {}", response.getStatus());
        logger.info("🔹 Data/Hora: {}", LocalDateTime.now());
        logger.info("⏳ Tempo de execução: {} ms", duration);

    }


}
