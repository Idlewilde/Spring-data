package exam.service.impl;

import exam.model.Town;
import exam.model.dto.TownImportDTO;
import exam.model.dto.TownRootImportDTO;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TownServiceImpl implements TownService {

    private TownRepository townRepository;
    private final JAXBContext context;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) throws JAXBException {
        this.townRepository = townRepository;
        this.context = JAXBContext.newInstance(TownRootImportDTO.class);
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Helper.returnStringOfFile("towns.xml");
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        TownRootImportDTO DTOs = (TownRootImportDTO)
                Helper.unmarshaller(context)
                        .unmarshal(new FileReader(Helper.createPath("towns.xml")
                                .toAbsolutePath().toString()));
        List<TownImportDTO> toImport = DTOs.getTowns();
        List<String> result = new ArrayList<>();

        for (TownImportDTO dto : toImport) {
            Optional<Town> town = this.townRepository.findByName(dto.getName());
            Set<ConstraintViolation<TownImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (town.isEmpty() && violations.isEmpty()) {
                Town newTown = Helper.modelMap().map(dto, Town.class);
                this.townRepository.save(newTown);
                result.add(String.format("Successfully imported Town %s",
                        dto.getName()));
            } else {
                result.add("Invalid Town");
            }
        }
        return String.join("\n", result);
    }
}
