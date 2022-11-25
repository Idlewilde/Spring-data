package exam.service.impl;

import exam.model.Customer;
import exam.model.dto.CustomerImportDTO;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Helper.returnStringOfFile("customers.json");
    }

    @Override
    public String importCustomers() throws IOException {
        CustomerImportDTO[] dtos = Helper.importGson()
                .fromJson(readCustomersFileContent(), CustomerImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (CustomerImportDTO dto : dtos) {
            Optional<Customer> customer =
                    this.customerRepository.findByEmail(dto.getEmail());
            Set<ConstraintViolation<CustomerImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (customer.isEmpty() && violations.isEmpty()) {
                Customer newCustomer =
                        Helper.modelMap().map(dto, Customer.class);
                newCustomer.setTown(this.townRepository.findByName(dto.getTown().getName()).get());
                this.customerRepository.save(newCustomer);
                result.add(String.format("Successfully imported Customer %s %s - %s",
                        dto.getFirstName(), dto.getLastName(), dto.getEmail()));
            } else {
                result.add("Invalid Customer");
            }
        }
        return String.join("\n", result);
    }
}
