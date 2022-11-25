package com.example.football.service.impl;

import com.example.football.models.dto.ImportTownDTO;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
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
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Override
    public boolean areImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        Path path = Path.of("src","main","resources","files","json","towns.json");
        return String.join("\n",Files.readAllLines(path));
    }

    @Override
    public String importTowns() throws IOException {
        String json = readTownsFileContent();
        ImportTownDTO [] importTownDTOs = this.gson.fromJson(json, ImportTownDTO[].class);
        List<String> result = new ArrayList<>();
        for (ImportTownDTO importTownDTO : importTownDTOs) {
            Optional <Town> town = this.townRepository.findByName(importTownDTO.getName());
            Set < ConstraintViolation<ImportTownDTO>> violations=
                    this.validator.validate(importTownDTO);
            if(violations.isEmpty()&& town.isEmpty()){
                Town newTown = modelMapper.map(importTownDTO, Town.class);
                this.townRepository.save(newTown);
                result.add(String.format("Successfully imported Town %s - %d",
                    importTownDTO.getName(),importTownDTO.getPopulation()));}
            else{result.add("Invalid Town");}
        }
        return String.join("\n", result);
    }
}
