package softuni.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartImportDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.Helper;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PartServiceImpl implements PartService {
    private PartRepository partRepository;

    @Autowired
    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }


    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Helper.returnStringOfFile("parts.json");
    }

    @Override
    public String importParts() throws IOException {
        PartImportDTO[] dtos = Helper.importGson()
                .fromJson(readPartsFileContent(), PartImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (PartImportDTO dto : dtos) {
            Set<ConstraintViolation<PartImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            Optional<Part> optionalPart =
                    this.partRepository.findByPartName(dto.getPartName());
            if (violations.isEmpty() && optionalPart.isEmpty()) {
                Part part =
                        Helper.modelMap().map(dto, Part.class);
                this.partRepository.save(part);
                result.add(String.format("Successfully imported part %s - %.2f",
                        dto.getPartName(), dto.getPrice()));
            } else {
                result.add("Invalid part");
            }
        }
        return String.join("\n", result);
    }
}
