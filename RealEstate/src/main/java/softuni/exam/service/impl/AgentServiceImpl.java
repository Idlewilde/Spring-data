package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentImportDTO;
import softuni.exam.models.dto.TownImportDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;

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
public class AgentServiceImpl implements AgentService {
    private AgentRepository agentRepository;
    private TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count()>0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        Path path = Path.of("src","main","resources","files","json","agents.json");
        return String.join("\n", Files.readAllLines(path));
    }

    @Override
    public String importAgents() throws IOException {
        String json = readAgentsFromFile();
        AgentImportDTO[] dtos = this.gson.fromJson(json, AgentImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (AgentImportDTO dto : dtos) {
            Optional<Agent> agent = this.agentRepository.findByFirstName(dto.getFirstName());
            Set<ConstraintViolation<AgentImportDTO>> violations =
                    this.validator.validate(dto);
            if (agent.isEmpty() && violations.isEmpty()) {
                Agent newAgent = modelMapper.map(dto,Agent.class);
                newAgent.setTown(this.townRepository.findByTownName(dto.getTown()).get());
                this.agentRepository.save(newAgent);
                result.add(String.format("Successfully imported agent %s - %s",
                        newAgent.getFirstName(),newAgent.getLastName()));
            } else {
                result.add("Invalid agent");
            }
        }
        return String.join("\n",result);
    }
}
