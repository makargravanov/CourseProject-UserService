package com.example.usersservice.Controller;

import com.example.usersservice.Data.TokenResponse;
import com.example.usersservice.Data.UserLoginData;
import com.example.usersservice.Data.UserRegisterData;
import com.example.usersservice.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Async
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<?>> createUser(@RequestBody UserRegisterData user) {
        return userService.createUser(user.getName(), user.getLogin(), user.getPassword(), user.getEmail());
    }
    @Async
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> loginUser(@RequestBody UserLoginData user) {
        System.out.println(user.getLoglogin() + " - " + user.getLogpassword());
        return userService.loginUser(user.getLoglogin(), user.getLogpassword());
    }


    @PostMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestBody TokenResponse token) {
        System.out.println("/profile " + token.getToken());

        return userService.getProfile(token.getToken());
    }
    @PostMapping("/getUserPlaylistById")
    public ResponseEntity<?> getUserPlaylistById(Long id){
        System.out.println("id= "+id);
        return userService.getUserPlaylistById(id);
    }
    @PostMapping("/addToPlaylist")
    public ResponseEntity<?> addToPlaylist(Long userId, Long trackId){
        System.out.println("UserId= "+userId+ " trackId= "+trackId);
        return userService.addToPlaylist(userId,trackId);
    }
}
