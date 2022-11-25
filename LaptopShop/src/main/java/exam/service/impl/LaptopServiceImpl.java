package exam.service.impl;

import exam.model.Laptop;
import exam.model.dto.LaptopImportDTO;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {
    private LaptopRepository laptopRepository;
    private ShopRepository shopRepository;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Helper.returnStringOfFile("laptops.json");
    }

    @Override
    public String importLaptops() throws IOException {
        LaptopImportDTO[] dtos = Helper.importGson()
                .fromJson(readLaptopsFileContent(), LaptopImportDTO[].class);
        List<String> result = new ArrayList<>();
        for (LaptopImportDTO dto : dtos) {
            Optional<Laptop> laptop = this.laptopRepository.findByMacAddress(dto.getMacAddress());
            Set<ConstraintViolation<LaptopImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (laptop.isEmpty() && violations.isEmpty()) {
                Laptop newLaptop = Helper.modelMap().map(dto, Laptop.class);
                newLaptop.setShop(this.shopRepository.findByName(dto.getShop().getName()).get());
                this.laptopRepository.save(newLaptop);
                result.add(String.format("Successfully imported Laptop %s - %s - %s - %s",
                        dto.getMacAddress(), dto.getCpuSpeed(), dto.getRam(), dto.getStorage()));
            } else {
                result.add("Invalid Laptop");
            }
        }
        return String.join("\n", result);
    }

    @Override
    public String exportBestLaptops() {
        return String.join("\n", this.laptopRepository.OrderByCpuSpeedDescRamDescMacAddressAsc()
                .stream()
                .map(Laptop::toString).collect(Collectors.toList()));
    }
}
