package exam.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helper {

    public static Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static ModelMapper modelMap() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
        modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ISO_LOCAL_TIME),
                String.class, LocalDate.class);
        return modelMapper;
    }

    public static Gson importGson() {
        return new GsonBuilder().create();
    }

    public static String returnStringOfFile(String fileName) throws IOException {
        if (fileName.endsWith("json")) {
            return String.join("\n", Files.readAllLines(Path.of
                    ("src", "main", "resources", "files", "json", fileName)));
        } else {
            return Files.readString(Path.of
                    ("src", "main", "resources", "files", "xml", fileName));
        }
    }

    public static Path createPath(String fileName) {
        if (fileName.endsWith("json")) {
            return Path.of("src", "main", "resources", "files", "json", fileName);
        } else {
            return Path.of("src", "main", "resources", "files", "xml", fileName);
        }
    }

    public static Unmarshaller unmarshaller(JAXBContext ctx) throws JAXBException {
        return ctx.createUnmarshaller();
    }
}
