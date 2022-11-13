package com.example.springintro.service;

import com.example.springintro.model.entity.DTOs.CreateGameDTO;
import com.example.springintro.model.entity.Game;
import com.example.springintro.model.entity.User;
import com.example.springintro.repository.GameRepository;
import com.example.springintro.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private final UserRepository userRepository;

    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Game addGame(CreateGameDTO createGameDTO) {
        User userToLogOut = this.userRepository.findByIsLogged(true);
        if (userToLogOut == null || !userToLogOut.isAdministrator()) {
            throw new IllegalArgumentException("User not logged or does not have admin rights");
        } else {
            createGameDTO.validate();
            Game toSave = MODEL_MAPPER.map(createGameDTO, Game.class);
            this.gameRepository.save(toSave);
            return toSave;
        }
    }

    @Override
    public Game editGame(String[] commandParts) {
        User userToLogOut = this.userRepository.findByIsLogged(true);
        if (userToLogOut == null || !userToLogOut.isAdministrator()) {
            throw new IllegalArgumentException("User not logged or does not have admin rights");
        } else {
            Game game = gameRepository.findById(Long.parseLong(commandParts[1]));
            if (game == null) {
                throw new IllegalArgumentException("Game not in database!");
            } else {
                for (int i = 2; i < commandParts.length; i++) {
                    String[] currentCommandParts = commandParts[i].split("=");
                    String value = currentCommandParts[1];
                    switch (currentCommandParts[0]) {
                        case "title":
                            if (!value.substring(0, 1).toUpperCase().equals(value.substring(0, 1))
                                    || value.length() > 100 || value.length() < 3) {
                                throw new IllegalArgumentException("Title not in correct format!");
                            } else {
                                game.setTitle(value);
                            }
                            break;
                        case "price":
                            if (Double.parseDouble(value) < 0) {
                                throw new IllegalArgumentException("Price not in correct format");
                            } else {
                                game.setPrice(Double.parseDouble(value));
                            }
                            ;
                            break;
                        case "size":
                            if (Double.parseDouble(value) < 0) {
                                throw new IllegalArgumentException("Size not in correct format");
                            } else {
                                game.setSize(Double.parseDouble(value));
                            }
                            ;
                            break;
                        case "trailer":
                            String id = value.split("=")[value.split("=").length - 1];
                            if (id.length() != 11) {
                                throw new IllegalArgumentException("Id not in correct format!");
                            } else {
                                game.setTrailer(id);
                            }
                            break;
                        case "thumbnail":
                            if (!value.startsWith("https://")
                                    && !value.startsWith("http://")) {
                                throw new IllegalArgumentException("Thumbnail url not in correct format!");
                            } else {
                                game.setThumbnail(value);
                            }
                            break;
                        case "Release date":
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            game.setReleaseDate(LocalDate.parse(value, formatter));
                            break;
                        case "description":
                            if (value.length() < 20) {
                                throw new IllegalArgumentException("Too short description!");
                            } else {
                                game.setDescription(value);
                            }
                            break;
                    }
                }
            }
            gameRepository.save(game);

            return game;
        }
    }

    @Override
    public String deleteGame(long id) {
        Game game = gameRepository.findById(id);
        if (game == null) {
            return "No such game in database";
        } else {
            gameRepository.delete(game);
            return "Game successfully deleted!";
        }
    }

    @Override
    public String getAllGamesInfo() {
        StringBuilder sb = new StringBuilder();
        List<Game> games = gameRepository.findAll();
        if (games.size() == 0) {
            return "No games in DB";
        } else {
            for (Game game : games) {
                sb.append(String.format("%s %.2f%n", game.getTitle(), game.getPrice()));
            }
            return sb.toString();
        }
    }

    @Override
    public String getDetails(String title) {
        Game game = gameRepository.findByTitle(title);
        if (game == null) {
            return "No such game in DB!";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Title: %s%n", title))
                    .append(String.format("Price: %.2f%n", game.getPrice()))
                    .append(String.format("Description: %s%n", game.getDescription()))
                    .append(String.format("Release Date: %s", game.getReleaseDate().toString()));
            return sb.toString();
        }
    }


}
