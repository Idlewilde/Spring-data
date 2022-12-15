package softuni.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskImportDTO;
import softuni.exam.models.dto.TaskRootImportDTO;
import softuni.exam.models.entity.CarType;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
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
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private MechanicRepository mechanicRepository;
    private PartRepository partRepository;
    private CarRepository carRepository;
    private final JAXBContext context;
    private final Unmarshaller unmarshaller;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, CarRepository carRepository,
                           PartRepository partRepository, MechanicRepository mechanicRepository)
            throws JAXBException {
        this.taskRepository = taskRepository;
        this.context = JAXBContext.newInstance(TaskRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.partRepository = partRepository;
        this.carRepository = carRepository;
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Helper.returnStringOfFile("tasks.xml");
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        TaskRootImportDTO DTOs = (TaskRootImportDTO)
                this.unmarshaller.unmarshal(new FileReader
                        (Helper.createPath("tasks.xml")
                                .toAbsolutePath().toString()));
        List<TaskImportDTO> toImport = DTOs.getTasks();
        List<String> result = new ArrayList<>();

        for (TaskImportDTO dto : toImport) {

            Optional<Mechanic> mechanic = this.mechanicRepository.findByFirstName(dto.getMechanic().getFirstName());
            Set<ConstraintViolation<TaskImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (mechanic.isPresent() && violations.isEmpty()) {
                Task task = Helper.modelMap().map(dto, Task.class);
                task.setCar(this.carRepository.getById(dto.getCar().getId()));
                task.setMechanic(mechanic.get());
                task.setPart(this.partRepository.getById(dto.getPart().getId()));

                this.taskRepository.save(task);

                result.add(String.format("Successfully imported task %.2f",
                        task.getPrice()));
            } else {
                result.add("Invalid task");
            }
        }

        return String.join("\n", result);

    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        List<Task> tasks = this.taskRepository.findByCarCarTypeOrderByPriceDesc(CarType.coupe);
        List<String> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task.toString());
        }
        return String.join("\n", result);
    }
}
