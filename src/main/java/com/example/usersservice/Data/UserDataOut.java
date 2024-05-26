package com.example.usersservice.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataOut {
    private Long id;
    private String name;
    private String login;
    private String email;
}
