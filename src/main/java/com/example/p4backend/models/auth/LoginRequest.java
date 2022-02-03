package com.example.p4backend.models.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
