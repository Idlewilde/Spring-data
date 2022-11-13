package com.example.springintro.service;

import com.example.springintro.model.entity.DTOs.LoginDTO;
import com.example.springintro.model.entity.DTOs.RegisterDTO;
import com.example.springintro.model.entity.Game;
import com.example.springintro.model.entity.User;
import com.example.springintro.repository.GameRepository;
import com.example.springintro.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final GameRepository gameRepository;
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }


    @Override
    public User register(RegisterDTO registerDTO) {
        registerDTO.validate();
        User toRegister = MODEL_MAPPER.map(registerDTO, User.class);
        long count = this.userRepository.count();
        if (count == 0) {
            toRegister.setAdministrator(true);
        }
        return this.userRepository.save(toRegister);
    }

    @Override
    public User login(LoginDTO loginDTO) {
        User userToLogIN = this.userRepository.findByEmail(loginDTO.getEmail());
        if (userToLogIN == null) {
            throw new IllegalArgumentException("User not registered!");
        } else if (!loginDTO.getPassword().equals(userToLogIN.getPassword())) {
            throw new IllegalArgumentException("Password incorrect!");
        } else {
            userToLogIN.setLogged(true);
            this.userRepository.save(userToLogIN);
            return userToLogIN;
        }
    }

    @Override
    public String logout() {
        User userToLogOut = this.userRepository.findByIsLogged(true);
        if (userToLogOut == null) {
            return "No logged in user!";
        } else {
            userToLogOut.setLogged(false);
            this.userRepository.save(userToLogOut);
            return "User logged out!";
        }


    }

    @Override
    public String purchaseGame(long id) {
        Game game = this.gameRepository.findById(id);
        User loggedIn = this.userRepository.findByIsLogged(true);
        if (game == null || loggedIn == null) {
            throw new IllegalArgumentException("No such game or logged in user in database!");
        } else {
            loggedIn.purchaseGame(game);
            this.userRepository.save(loggedIn);
            return "Game successfully purchased!";
        }
    }

    @Override
    public String displayOwnedGames() {
        User loggedIn = this.userRepository.findByIsLogged(true);
        if (loggedIn == null) {
            throw new IllegalArgumentException("No such game or logged in user in database!");
        } else {
            StringBuilder sb = new StringBuilder();
            List<Game> games = new ArrayList<>(loggedIn.getGames());
            if (games.size() == 0) {
                return "User does not own games!";
            } else {
                for (Game game : games) {
                    sb.append(String.format("%s%n", game.getTitle()));
                }
                return sb.toString();
            }
        }
    }
}