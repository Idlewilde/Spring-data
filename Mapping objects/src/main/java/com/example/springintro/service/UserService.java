package com.example.springintro.service;

import com.example.springintro.model.entity.DTOs.LoginDTO;
import com.example.springintro.model.entity.DTOs.RegisterDTO;
import com.example.springintro.model.entity.User;

public interface UserService {

    User register(RegisterDTO registerDTO);

    User login(LoginDTO loginDTO);

    String logout();

    String purchaseGame(long id);

    String displayOwnedGames();

}
