package com.example.p4backend.models.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
