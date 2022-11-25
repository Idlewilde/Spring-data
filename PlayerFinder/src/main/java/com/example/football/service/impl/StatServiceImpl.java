package com.example.football.service.impl;

import com.example.football.models.dto.ImportStatDTO;
import com.example.football.models.dto.ImportStatRootDTO;
import com.example.football.models.dto.ImportTeamDTO;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.StatService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//ToDo - Implement all methods
@Service
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    private final Path path;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) throws JAXBException {
        this.statRepository = statRepository;
        this.context = JAXBContext.newInstance(ImportStatRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.path = Path.of("src", "main", "resources", "files", "xml", "stats.xml");
    }


    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importStats() throws FileNotFoundException, JAXBException {
        ImportStatRootDTO DTOs = (ImportStatRootDTO)
                this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        List<ImportStatDTO> toImport = DTOs.getStats();
        List<String> result = new ArrayList<>();
        for (ImportStatDTO importStatDTO : toImport) {
            Optional<Stat> stat = this.statRepository.findByEnduranceAndPassingAndShooting
                    (importStatDTO.getEndurance(), importStatDTO.getPassing(),
                            importStatDTO.getShooting());
            Set<ConstraintViolation<ImportStatDTO>> violations =
                    this.validator.validate(importStatDTO);
            if (violations.isEmpty() && stat.isEmpty()) {
                Stat newStat = modelMapper.map(importStatDTO, Stat.class);
                statRepository.save(newStat);
                result.add(String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                        importStatDTO.getEndurance(),
                        importStatDTO.getPassing(),
                        importStatDTO.getShooting()));
            } else {
                result.add("Invalid Stat");
            }
        }
        return String.join("\n", result);
    }
}
