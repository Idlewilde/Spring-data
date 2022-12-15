package softuni.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicImportDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.Helper;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MechanicServiceImpl implements MechanicService {

    private MechanicRepository mechanicRepository;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Helper.returnStringOfFile("mechanics.json");
    }

    @Override
    public String importMechanics() throws IOException {
        MechanicImportDTO[] dtos = Helper.importGson()
                .fromJson(readMechanicsFromFile(), MechanicImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (MechanicImportDTO dto : dtos) {
            Set<ConstraintViolation<MechanicImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            Optional<Mechanic> optionalMechanic =
                    this.mechanicRepository.findByEmail(dto.getEmail());
            if (violations.isEmpty() && optionalMechanic.isEmpty()) {
                Mechanic mechanic =
                        Helper.modelMap().map(dto, Mechanic.class);
                this.mechanicRepository.save(mechanic);
                result.add(String.format("Successfully imported mechanic %s %s",
                        dto.getFirstName(), dto.getLastName()));
            } else {
                result.add("Invalid mechanic");
            }
        }
        return String.join("\n", result);
    }
}
