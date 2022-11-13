package com.example.springintro;

import com.example.springintro.model.entity.DTOs.CreateGameDTO;
import com.example.springintro.model.entity.DTOs.LoginDTO;
import com.example.springintro.model.entity.DTOs.RegisterDTO;
import com.example.springintro.model.entity.Game;
import com.example.springintro.model.entity.User;
import com.example.springintro.service.GameService;
import com.example.springintro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserService userService;
    private final GameService gameService;


    @Autowired
    public CommandLineRunnerImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    private String execute(String commandLine) throws ParseException {
        String[] commandParts = commandLine.split("\\|");
        String commandName = commandParts[0];

        return switch (commandName) {
            case "RegisterUser" -> {
                User user = userService.register(new RegisterDTO(commandParts));
                yield String.format("%s was registered", user.getFullName());
            }
            case "LoginUser" -> {
                User user = userService.login(new LoginDTO(commandParts));
                yield String.format("%s was logged in", user.getFullName());
            }
            case "Logout" -> userService.logout();
            case "AddGame" -> {
                Game game = gameService.addGame(new CreateGameDTO(commandParts));
                yield String.format("Game %s is created.", game.getTitle());
            }
            case "EditGame" -> {
                Game game = gameService.editGame(commandParts);
                yield String.format("%s edited", game.getTitle());
            }
            case "DeleteGame" -> gameService.deleteGame(Long.parseLong(commandParts[1]));
            case "AllGames" -> gameService.getAllGamesInfo();
            case "DetailGame" -> gameService.getDetails(commandParts[1]);
            case "PurchaseGame" -> userService.purchaseGame(Long.parseLong(commandParts[1]));
            case "OwnedGames" -> userService.displayOwnedGames();
            default -> "Unknown Command";
        };
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        try {
            System.out.println(execute(command));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }
}
