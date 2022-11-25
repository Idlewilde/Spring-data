package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastImportDTO;
import softuni.exam.models.dto.ForecastRootImportDTO;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;

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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {
    ForecastRepository forecastRepository;
    CityRepository cityRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;
    private final Path path;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.context = JAXBContext.newInstance(ForecastRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.path = Path.of("src", "main", "resources", "files", "xml", "forecasts.xml");
        this.modelMapper.addConverter(ctx -> LocalTime.parse(ctx.getSource(), DateTimeFormatter.ISO_LOCAL_TIME),
                String.class, LocalTime.class);
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count()>0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
            return Files.readString(path);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
            ForecastRootImportDTO DTOs = (ForecastRootImportDTO)
                    this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
            List<ForecastImportDTO> toImport = DTOs.getForecasts();
            List<String> result = new ArrayList<>();
        for (ForecastImportDTO dto : toImport) {
            Optional<Forecast> forecast =
                    this.forecastRepository.findByDayOfWeekAndCityId(dto.getDayOfWeek(),dto.getCity());
            Set<ConstraintViolation<ForecastImportDTO>> violations =
                    this.validator.validate(dto);

            if(forecast.isEmpty()&&violations.isEmpty()){
                Forecast newForecast = this.modelMapper.map(dto, Forecast.class);
                newForecast.setCity(this.cityRepository.getById(dto.getCity()));
                this.forecastRepository.save(newForecast);
                result.add(String.format("Successfully import forecast %s - %.2f",
                        newForecast.getDayOfWeek().toString(), newForecast.getMaxTemperature()));
            }
            else{result.add("Invalid forecast");}
        }
        return String.join("\n",result);
    }

    @Override
    public String exportForecasts() {
        List <Forecast> forecasts = this.forecastRepository
                                   .findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc
                                   (DayOfWeek.SUNDAY,150000);
        List <String> result = forecasts.stream().map(Forecast::toString).collect(Collectors.toList());
        return String.join("\n",result);
    }
}
