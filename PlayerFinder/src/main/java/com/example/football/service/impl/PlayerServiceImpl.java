package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayerDTO;
import com.example.football.models.dto.ImportPlayerRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//ToDo - Implement all methods
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final StatRepository statRepository;
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    private final Path path;


    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, StatRepository statRepository,
                             TeamRepository teamRepository, TownRepository townRepository)
            throws JAXBException {
        this.playerRepository = playerRepository;
        this.statRepository = statRepository;
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.context = JAXBContext.newInstance(ImportPlayerRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.path = Path.of("src", "main", "resources", "files", "xml", "players.xml");
        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importPlayers() throws FileNotFoundException, JAXBException {
        ImportPlayerRootDTO DTOs = (ImportPlayerRootDTO)
                this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        List<ImportPlayerDTO> toImport = DTOs.getPlayers();
        List<String> result = new ArrayList<>();

        for (ImportPlayerDTO importPlayerDTO : toImport) {

            Optional<Player> player = this.playerRepository.findByEmail(importPlayerDTO.getEmail());
            Set<ConstraintViolation<ImportPlayerDTO>> violations =
                    this.validator.validate(importPlayerDTO);
            if (player.isEmpty() && violations.isEmpty()) {
                Player newPlayer = this.modelMapper.map(importPlayerDTO, Player.class);
                newPlayer.setStat(this.statRepository.findById(importPlayerDTO.getStatId().getId()).get());
                newPlayer.setTeam(this.teamRepository.findByName(importPlayerDTO.getTeam().getName()).get());
                newPlayer.setTown(this.townRepository.findByName(importPlayerDTO.getTown().getName()).get());

                this.playerRepository.save(newPlayer);

                result.add(String.format("Successfully imported Player %s %s - %s",
                        importPlayerDTO.getFirstName(),
                        importPlayerDTO.getLastName(),
                        importPlayerDTO.getPosition()));
            } else {
                result.add("Invalid player");
            }
        }

        return String.join("\n", result);

    }

    @Override
    public String exportBestPlayers() {
        LocalDate before = LocalDate.parse("01/01/2003", DateTimeFormatter.ofPattern("dd/MM/yyy"));
        LocalDate after = LocalDate.parse("01/01/1995", DateTimeFormatter.ofPattern("dd/MM/yyy"));
        List<Player> players = this.playerRepository
                .findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before);
        List<String> result = new ArrayList<>();
        for (Player player : players) {
            result.add(player.toString());
        }
        return String.join("\n", result);
    }
}
