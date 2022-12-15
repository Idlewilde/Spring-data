package softuni.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarImportDTO;
import softuni.exam.models.dto.CarRootImportDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.Helper;

import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) throws JAXBException {
        this.carRepository = carRepository;
        this.context = JAXBContext.newInstance(CarRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Helper.returnStringOfFile("cars.xml");
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        CarRootImportDTO DTOs = (CarRootImportDTO)
                this.unmarshaller
                        .unmarshal(new FileReader
                                (Helper.createPath("cars.xml")
                                        .toAbsolutePath().toString()));
        List<CarImportDTO> toImport = DTOs.getCars();
        List<String> result = new ArrayList<>();

        for (CarImportDTO dto : toImport) {

            Optional<Car> car = this.carRepository.findByPlateNumber(dto.getPlateNumber());
            Set<ConstraintViolation<CarImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (car.isEmpty() && violations.isEmpty()) {
                Car newCar = Helper.modelMap().map(dto, Car.class);
                this.carRepository.save(newCar);
                result.add(String.format("Successfully imported car %s - %s",
                        dto.getMake(), dto.getModel()));
            } else {
                result.add("Invalid car");
            }
        }
        return String.join("\n", result);
    }
}
