package com.example.usersservice.Service;

import com.example.usersservice.Data.TokenResponse;
import com.example.usersservice.Data.UserRegisterData;
import com.example.usersservice.Entity.MusicalTrackKeysEntity;
import com.example.usersservice.Entity.UserEntity;
import com.example.usersservice.Mapper.MapperExample;
import com.example.usersservice.Repository.MusicalTrackKeysRepository;
import com.example.usersservice.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceDB {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DefaultTokenService defaultTokenService;
    private final MapperExample mapperExample;
    private final MusicalTrackKeysRepository musicalTrackKeysRepository;

    public UserServiceDB(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, DefaultTokenService defaultTokenService, MapperExample mapperExample, MusicalTrackKeysRepository musicalTrackKeysRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.defaultTokenService = defaultTokenService;
        this.mapperExample = mapperExample;
        this.musicalTrackKeysRepository = musicalTrackKeysRepository;
    }


    @Async
    public CompletableFuture<ResponseEntity<?>> createUser(UserRegisterData user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBody = mapper.createObjectNode();
        try {
            CompletableFuture<UserEntity> optional = userRepository.getByLogin(user.getLogin());
            UserEntity userEntity = optional.get();
            if (userEntity != null) {
                responseBody.put("message", "User with this login already exists");
                System.out.println("User with this login already exists");
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(responseBody));
            }
        } catch (InterruptedException | ExecutionException e) {
            responseBody.put("message", "problem with login");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(responseBody));
        }
        try {
            CompletableFuture<UserEntity> optional = userRepository.getByEmail(user.getEmail());
            UserEntity userEntity = optional.get();
            if (userEntity != null) {
                responseBody.put("message", "User with this email already exists");
                System.out.println("User with this email already exists");
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(responseBody));
            }
        } catch (InterruptedException | ExecutionException e) {
            responseBody.put("message", "problem with email");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(responseBody));
        }

        UserEntity userEntity = mapperExample.registerDataToUserEntity(user);
        userRepository.save(userEntity);

        responseBody.put("message", "Success");

        return CompletableFuture.completedFuture(ResponseEntity.ok(responseBody));
    }

    @Async
    public CompletableFuture<ResponseEntity<?>> loginUserDB(String login, String password) {

            CompletableFuture<UserEntity> optional = userRepository.getByLogin(login);
        try {
            UserEntity userEntity = optional.get();
            if (!bCryptPasswordEncoder.matches(password, userEntity.getPassword())) {
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect login or password"));
            }else{
                String token = defaultTokenService.generateToken(userEntity.getId());
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(token);
                return CompletableFuture.completedFuture(ResponseEntity.ok(tokenResponse));
            }
        } catch (InterruptedException | ExecutionException e) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect login or password"));
        } catch (NullPointerException e){
            System.out.println(login + " " + password);
            System.out.println("NPE!!!");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect login or password"));
        }
    }


    public ResponseEntity<?> findById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(mapperExample.userEntityToUserDataOut(user.get()));
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    public ResponseEntity<?> getUserPlaylistById(Long id){
        Optional<UserEntity> opt= userRepository.findById(id);
        UserEntity user = new UserEntity();
        if(opt.isPresent()){
            user = opt.get();
        }else{
            System.out.println("not found");
            return ResponseEntity.badRequest().body("Not found");
        }
        Set<UserEntity> users = new HashSet<>();
        users.add(user);
        Optional<ArrayList<MusicalTrackKeysEntity>> optKeys = musicalTrackKeysRepository.findAllByUsers(users);
        if(optKeys.isPresent()){
            ArrayList<MusicalTrackKeysEntity> keysEntityArrayList= optKeys.get();
            ArrayList<Long> keys = new ArrayList<>();
            for (MusicalTrackKeysEntity entity: keysEntityArrayList){
                System.out.println(entity.getMusicalTrackKey());
                keys.add(entity.getMusicalTrackKey());
            }
            System.out.println("keys");
            return ResponseEntity.ok(keys);
        }else{
            ArrayList<Long> empty = new ArrayList<>();
            System.out.println("empty");
            return ResponseEntity.ok(empty);
        }
    }
    public ResponseEntity<?> addToPlaylist(Long userId, Long trackId){
        Optional<UserEntity> opt = userRepository.findById(userId);
        if (opt.isEmpty()){
            return ResponseEntity.badRequest().body("User not found");
        }
        Optional<MusicalTrackKeysEntity> opt2 = musicalTrackKeysRepository.findByMusicalTrackKey(trackId);
        MusicalTrackKeysEntity trackForSave = new MusicalTrackKeysEntity();
        if(opt2.isPresent()){
            trackForSave = opt2.get();
        }else{
            MusicalTrackKeysEntity track = new MusicalTrackKeysEntity();
            track.setMusicalTrackKey(trackId);
            trackForSave= musicalTrackKeysRepository.save(track);
        }

        UserEntity user = opt.get();
        user.getTracks().add(trackForSave);
        trackForSave.getUsers().add(user);
        userRepository.save(user);
        musicalTrackKeysRepository.save(trackForSave);
        return ResponseEntity.ok("success");
    }
    public UserEntity getUserByLogin(String login){
        CompletableFuture<UserEntity> future = userRepository.getByLogin(login);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NullPointerException("User not found");
        }
    }
}
