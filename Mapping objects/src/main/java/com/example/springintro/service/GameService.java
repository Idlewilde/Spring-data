package com.example.springintro.service;

import com.example.springintro.model.entity.DTOs.CreateGameDTO;
import com.example.springintro.model.entity.Game;

public interface GameService {

    Game addGame (CreateGameDTO createGameDTO);
    Game editGame (String [] commandParts);

    String deleteGame (long id);

    String getAllGamesInfo ();

    String getDetails(String title);
}
