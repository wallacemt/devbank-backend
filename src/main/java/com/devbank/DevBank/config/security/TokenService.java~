package com.devbank.DevBank.config.security;

import com.devbank.DevBank.entities.User.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.Instant;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User usuario, long expirationTime){
        Instant expirationInstant = Instant.now().plusMillis(expirationTime);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API devbank-login")
                    .withSubject(usuario.getUsername())
                    .withSubject(usuario.getCpf())
                    .withExpiresAt(expirationInstant)
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt",exception);
        }
    }

    public String generateToken(User usuario) {
        long defaultExpirationTime = 3 * 60 * 60 * 1000;
        return generateToken(usuario, defaultExpirationTime);
    }

    public String validationToken(String tokenJWT){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String subject = JWT.require(algorithm)
                    .withIssuer("API devbank-login")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
            return subject;
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}
