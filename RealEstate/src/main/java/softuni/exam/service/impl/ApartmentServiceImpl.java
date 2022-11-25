package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentImportDTO;
import softuni.exam.models.dto.ApartmentRootImportDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private ApartmentRepository apartmentRepository;
    private TownRepository townRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    private final Path path;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository) throws JAXBException {
        this.apartmentRepository = apartmentRepository;
        this.context = JAXBContext.newInstance(ApartmentRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.townRepository = townRepository;
        this.path = Path.of("src", "main", "resources", "files", "xml", "apartments.xml");
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count()>0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        ApartmentRootImportDTO DTOs = (ApartmentRootImportDTO)
                this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        List<ApartmentImportDTO> toImport = DTOs.getApartments();
        List<String> result = new ArrayList<>();
        for (ApartmentImportDTO dto : toImport) {
            Optional<Apartment> app =
                    this.apartmentRepository.findByTownTownNameAndArea(dto.getTown(),dto.getArea());
            Set<ConstraintViolation<ApartmentImportDTO>> violations =
                    this.validator.validate(dto);

            if(app.isEmpty()&&violations.isEmpty()){
                Apartment apartment = this.modelMapper.map(dto, Apartment.class);
                apartment.setTown(this.townRepository.getByTownName(dto.getTown()));
                this.apartmentRepository.save(apartment);
                result.add(String.format("Successfully imported apartment %s - %.2f",
                        apartment.getApartmentType().toString(), apartment.getArea()));
            }
            else{result.add("Invalid apartment");}
        }
        return String.join("\n",result);
    }
}