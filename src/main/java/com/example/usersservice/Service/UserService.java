package com.example.usersservice.Service;

import com.example.usersservice.Data.UserRegisterData;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final UserServiceDB userServiceDB;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DefaultTokenService defaultTokenService;
    public UserService(UserServiceDB userServiceDB, BCryptPasswordEncoder bCryptPasswordEncoder, DefaultTokenService defaultTokenService) {
        this.userServiceDB = userServiceDB;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.defaultTokenService = defaultTokenService;
    }

    public CompletableFuture<ResponseEntity<?>> createUser(String name, String login, String password, String email) {
        if(name.isEmpty() || login.isEmpty() || password.isEmpty() || email.isEmpty()) {
            System.out.println("Empty fields");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Empty fields"));
        }else if(!isValidEmailAddress(email)) {
            System.out.println("Incorrect email "+email);
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect email"));
        }else if(!(password.length() > 8 && password.length() < 20)) {
            System.out.println("Incorrect password");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect password"));
        }else if(!(name.length() < 20)) {
            System.out.println("Incorrect name");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect name"));
        }else if(!(login.length() > 8 && login.length() < 20)) {
            System.out.println("Incorrect login");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Incorrect login"));
        }
        password = bCryptPasswordEncoder.encode(password);
        UserRegisterData userRegisterData = new UserRegisterData(name, login, password, email);
        return userServiceDB.createUser(userRegisterData);
    }
    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    @Async
    public CompletableFuture<ResponseEntity<?>> loginUser(String login, String password){
        return userServiceDB.loginUserDB(login, password);
    }

    public ResponseEntity<?> getProfile(String token){
        try {
            System.out.println("1");
            CompletableFuture<Boolean> checkToken = defaultTokenService.checkToken(token);
            Boolean check = checkToken.get();
            if(check) {
                System.out.println("2");
                return ResponseEntity.ok(userServiceDB.findById(defaultTokenService.getIdFromToken(token)));
            }else{
                System.out.println("3");
                return ResponseEntity.badRequest().body("Invalid token");
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("4");
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
    public ResponseEntity<?> getUserPlaylistById(Long id){
        return userServiceDB.getUserPlaylistById(id);
    }
    public ResponseEntity<?> addToPlaylist(Long userId,Long trackId){
        return userServiceDB.addToPlaylist(userId,trackId);
    }
}
