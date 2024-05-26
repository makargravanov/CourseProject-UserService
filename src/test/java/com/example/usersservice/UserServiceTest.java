package com.example.usersservice;

import com.example.usersservice.Data.UserRegisterData;
import com.example.usersservice.Service.DefaultTokenService;
import com.example.usersservice.Service.UserService;
import com.example.usersservice.Service.UserServiceDB;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserServiceDB userServiceDB;
    @Mock
    private UserRegisterData userRegisterData;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private DefaultTokenService defaultTokenService;

    @Before
    public void setup(){
        userServiceDB = Mockito.mock(UserServiceDB.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        defaultTokenService = Mockito.mock(DefaultTokenService.class);
        userService = new UserService(userServiceDB, bCryptPasswordEncoder, defaultTokenService);
        userRegisterData = Mockito.mock(UserRegisterData.class);
        Mockito.when(userServiceDB.createUser(userRegisterData)).thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok("OK")));
    }
    @Test
    public void testCreateUserWithToShortPassword() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("name", "normallogin", "passwor", "example@email.com");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect password"), responseEntity);
    }
    @Test
    public void testCreateUserWithToLongPassword() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("name", "normallogin", "megagigasuperlongpassword", "example@email.com");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect password"), responseEntity);
    }
    @Test
    public void testCreateUserWithInvalidEmail() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("name", "normallogin", "password1", "not email");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect email"), responseEntity);
    }
    @Test
    public void testCreateUserWithInvalidName() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("twentytwosymbolsstring", "login", "password1", "example@email.com");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect name"), responseEntity);
    }
    @Test
    public void testCreateUserWithToLongLogin() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("name", "twentyonesymbolslogin", "password1", "example@email.com");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect login"), responseEntity);
    }
    @Test
    public void testCreateUserWithToShortLogin() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("name", "log", "password1", "example@email.com");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Incorrect login"), responseEntity);
    }
    @Test
    public void testCreateUserWithEmptyFields() throws ExecutionException, InterruptedException {
        setup();
        CompletableFuture<ResponseEntity<?>> response = userService.createUser("", "", "", "");
        ResponseEntity<?> responseEntity = response.get();
        Assertions.assertEquals(ResponseEntity.badRequest().body("Empty fields"), responseEntity);
    }

}
