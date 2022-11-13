package com.example.springintro.model.entity.DTOs;


import org.springframework.validation.annotation.Validated;

@Validated
public class RegisterDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;

    public RegisterDTO(String[] commandParts) {
        this.email = commandParts[1];
        this.password = commandParts[2];
        this.confirmPassword = commandParts[3];
        this.fullName = commandParts[4];
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void validate() {
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Incorrect email.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords must match!");
        }
        char[] arr = password.toCharArray();
        boolean isDigit = false;
        for (char c : arr) {
            if (Character.isDigit(c)) {
                isDigit = true;
                break;
            }
        }
        if (password.toLowerCase().equals(password)
                || password.toUpperCase().equals(password)
                || !isDigit) {
            throw new IllegalArgumentException("Password not valid!");
        }

    }


}
