package com.example.usersservice.Repository;

import com.example.usersservice.Entity.UserEntity;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Async
    public CompletableFuture<UserEntity> getByLogin(String login);
    @Async
    public CompletableFuture<UserEntity> getByEmail(String Email);

    public Optional<UserEntity> findById(Long id);
}
