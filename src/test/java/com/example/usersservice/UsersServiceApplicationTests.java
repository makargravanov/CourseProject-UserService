package com.example.usersservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class UsersServiceApplicationTests {

    @Test
    public void testCreateUserWithToShortPassword() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithToShortPassword();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserWithToLongPassword() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithToLongPassword();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserWithInvalidEmail() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithInvalidEmail();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateUserWithInvalidName() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithInvalidName();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserWithToLongLogin() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithToLongLogin();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserWithToShortLogin() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithToShortLogin();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testCreateUserWithEmptyFields() {
        UserServiceTest userServiceTest = new UserServiceTest();
        try {
            userServiceTest.testCreateUserWithEmptyFields();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
