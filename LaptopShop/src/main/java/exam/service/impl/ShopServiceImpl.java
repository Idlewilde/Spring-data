package exam.service.impl;

import exam.model.Shop;
import exam.model.dto.ShopImportDTO;
import exam.model.dto.ShopRootImportDTO;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
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
public class ShopServiceImpl implements ShopService {

    private ShopRepository shopRepository;
    private TownRepository townRepository;
    private final JAXBContext context;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository) throws JAXBException {
        this.shopRepository = shopRepository;
        this.context = JAXBContext.newInstance(ShopRootImportDTO.class);
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Helper.returnStringOfFile("shops.xml");
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        ShopRootImportDTO DTOs = (ShopRootImportDTO)
                Helper.unmarshaller(context)
                        .unmarshal(new FileReader
                                (Helper.createPath("shops.xml")
                                        .toAbsolutePath().toString()));
        List<ShopImportDTO> toImport = DTOs.getShops();
        List<String> result = new ArrayList<>();

        for (ShopImportDTO dto : toImport) {

            Optional<Shop> shop = this.shopRepository.findByName(dto.getName());
            Set<ConstraintViolation<ShopImportDTO>> violations =
                    Helper.getValidator().validate(dto);
            if (shop.isEmpty() && violations.isEmpty()) {
                Shop newShop = Helper.modelMap().map(dto, Shop.class);
                newShop.setTown(this.townRepository.findByName(dto.getTown().getName()).get());
                this.shopRepository.save(newShop);
                result.add(String.format("Successfully imported Shop %s - %.2f",
                        dto.getName(), dto.getIncome()));
            } else {
                result.add("Invalid Shop");
            }
        }
        return String.join("\n", result);
    }
}
