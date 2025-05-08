package com.devbank.DevBank.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.devbank.DevBank.entities.User.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.Instant;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User usuario, long expirationTime) {
        Instant expirationInstant = Instant.now().plusMillis(expirationTime);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API devbank-login")
                    .withSubject(usuario.getEmail())
                    .withClaim("userId", usuario.getId().toString())
                    .withExpiresAt(expirationInstant)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public String generateToken(User usuario) {
        long defaultExpirationTime = Duration.ofHours(3).toMillis();

        return generateToken(usuario, defaultExpirationTime);
    }

    public String getUserIdFromToken(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer("API devbank-login")
                    .build()
                    .verify(tokenJWT);

            return decoded.getClaim("userId").asString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}
