package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityImportDTO;
import softuni.exam.models.dto.CountryImportDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CityServiceImpl implements CityService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    public CityServiceImpl(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count()>0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        Path path = Path.of("src","main","resources","files","json","cities.json");
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importCities() throws IOException {
        String json = readCitiesFileContent();
        CityImportDTO[] dtos = this.gson.fromJson(json, CityImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (CityImportDTO dto : dtos) {
            Optional<City> city = this.cityRepository.findByCityName(dto.getCityName());
            Set<ConstraintViolation<CityImportDTO>> violations =
                    this.validator.validate(dto);
            if (city.isEmpty() && violations.isEmpty()) {
                City newCity = modelMapper.map(dto,City.class);
                newCity.setCountry(this.countryRepository.findById(dto.getCountry()).get());
                this.cityRepository.save(newCity);
                result.add(String.format("Successfully imported city %s - %d",
                        newCity.getCityName(),newCity.getPopulation()));
            } else {
                result.add("Invalid city");
            }
        }
        return String.join("\n",result);
    }
}
