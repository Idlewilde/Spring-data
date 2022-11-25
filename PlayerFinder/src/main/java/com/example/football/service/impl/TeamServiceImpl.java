package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamDTO;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//ToDo - Implement all methods
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Override
    public boolean areImported() {
        return this.teamRepository.count()>0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        Path path = Path.of("src","main","resources","files","json","teams.json");
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTeams() throws IOException {
        String json = readTeamsFileContent();
        ImportTeamDTO[] importTeamDTOs = this.gson.fromJson(json, ImportTeamDTO[].class);
        List<String> result = new ArrayList<>();
        for (ImportTeamDTO importTeamDTO : importTeamDTOs) {
            Optional<Team> team = this.teamRepository.findByName(importTeamDTO.getName());
            Set<ConstraintViolation<ImportTeamDTO>> violations=
                    this.validator.validate(importTeamDTO);
            if(violations.isEmpty()&& team.isEmpty()){
            Team newTeam = this.modelMapper.map(importTeamDTO,Team.class);
            newTeam.setTown(this.townRepository.findByName(importTeamDTO.getTownName()).get());
                this.teamRepository.save(newTeam);
                result.add(String.format("Successfully imported Team %s - %d",
                        importTeamDTO.getName(),importTeamDTO.getFanBase()));}
            else{result.add("Invalid Team");}
        }
        return String.join("\n", result);

    }
}
