package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownImportDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;

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

@Service
public class TownServiceImpl implements TownService {

    private TownRepository townRepository;
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
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importTowns() throws IOException {
        String json = readTownsFileContent();
        TownImportDTO[] dtos = this.gson.fromJson(json, TownImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (TownImportDTO dto : dtos) {
            Optional<Town> town = this.townRepository.findByTownName(dto.getTownName());
            Set<ConstraintViolation<TownImportDTO>> violations =
                    this.validator.validate(dto);
            if (town.isEmpty() && violations.isEmpty()) {
                Town newTown = modelMapper.map(dto,Town.class);
                this.townRepository.save(newTown);
                result.add(String.format("Successfully imported town %s - %d",
                        newTown.getTownName(),newTown.getPopulation()));
            } else {
                result.add("Invalid town");
            }
        }
        return String.join("\n",result);
    }
}
