package com.example.springintro.model.entity.DTOs;

import org.springframework.validation.annotation.Validated;

@Validated
public class LoginDTO {
    private String email;
    private String password;

    public LoginDTO(String[] commandParts) {
        this.email = commandParts[1];
        this.password = commandParts[2];
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
