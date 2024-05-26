package com.example.usersservice.Service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.usersservice.Entity.UserEntity;
import com.example.usersservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class DefaultTokenService{
    private final UserRepository userRepository;

    @Value("${auth.jwt.secret}")
    private String secretKey;

    public DefaultTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant now = Instant.now();
        Instant exp = now.plus(120, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("userService")
                .withSubject(userId.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    @Async
    public CompletableFuture<Boolean> checkToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if (!decodedJWT.getIssuer().equals("userService")) {
                System.out.println("Issuer is incorrect");
                return CompletableFuture.completedFuture(false);
            }
            if (!checkId(Long.valueOf(decodedJWT.getSubject()))) {
                System.out.println("Id is incorrect");
                return CompletableFuture.completedFuture(false);
            }
        } catch (JWTVerificationException e) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(true);
    }
    public Long getIdFromToken(String checkedToken) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(checkedToken);
        return Long.valueOf(decodedJWT.getSubject());
    }
    private boolean checkId(Long id) {
        System.out.println(id.toString());
        Optional<UserEntity> optional = userRepository.findById(id);
        return optional.isPresent();
    }
}
