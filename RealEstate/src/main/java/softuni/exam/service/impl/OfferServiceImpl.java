package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferImportDTO;
import softuni.exam.models.dto.OfferRootImportDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;

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
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private OfferRepository offerRepository;
    private AgentRepository agentRepository;
    private ApartmentRepository apartmentRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    private final Path path;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentRepository agentRepository, ApartmentRepository apartmentRepository) throws JAXBException {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
        this.context = JAXBContext.newInstance(OfferRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.path = Path.of("src", "main", "resources", "files", "xml", "offers.xml");
        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count()>0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        OfferRootImportDTO DTOs = (OfferRootImportDTO)
                this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        System.out.println(DTOs);
        List<OfferImportDTO> toImport = DTOs.getOffers();
        List<String> result = new ArrayList<>();

        for (OfferImportDTO dto : toImport) {

            Optional<Agent> agent = this.agentRepository.findByFirstName(dto.getAgent().getName());
            Set<ConstraintViolation<OfferImportDTO>> violations =
                    this.validator.validate(dto);
            if (agent.isPresent() && violations.isEmpty()) {
                Offer offer = this.modelMapper.map(dto, Offer.class);
                offer.setAgent(agent.get());
                offer.setApartment(this.apartmentRepository.findById(dto.getApartment().getId()).get());

                this.offerRepository.save(offer);

                result.add(String.format("Successfully imported offer %.2f",
                        dto.getPrice()));
            } else {
                result.add("Invalid offer");
            }
        }

        return String.join("\n", result);

    }

    @Override
    public String exportOffers() {
        List <Offer> offers = this.offerRepository
                .findByApartmentApartmentTypeOrderByApartmentAreaDescPriceAsc
                        (ApartmentType.three_rooms);
        return String
                .join("\n",offers.stream()
                        .map(Offer::toString)
                        .collect(Collectors.toList()));
    }
}
