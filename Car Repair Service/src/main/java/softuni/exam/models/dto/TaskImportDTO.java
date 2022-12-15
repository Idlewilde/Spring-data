package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class TaskImportDTO {

    @XmlElement
    private String date;
    @XmlElement
    @Positive
    @NotNull
    private BigDecimal price;
    @XmlElement
    private CarXMLDTO car;
    @XmlElement
    private PartXMLDTO part;
    @XmlElement
    private MechanicImportDTO mechanic;

    public TaskImportDTO() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarXMLDTO getCar() {
        return car;
    }

    public void setCar(CarXMLDTO car) {
        this.car = car;
    }

    public PartXMLDTO getPart() {
        return part;
    }

    public void setPart(PartXMLDTO part) {
        this.part = part;
    }

    public MechanicImportDTO getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicImportDTO mechanic) {
        this.mechanic = mechanic;
    }
}
