package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CarImportDTO {
    @XmlElement
    @Enumerated
    private CarType carType;
    @XmlElement(name = "carMake")
    @Size(min = 2, max = 30)
    private String make;
    @XmlElement(name = "carModel")
    @Size(min = 2, max = 30)
    private String model;
    @XmlElement
    @Positive
    private int year;
    @XmlElement
    @Size(min = 2, max = 30)
    private String plateNumber;
    @XmlElement
    @Positive
    private int kilometers;
    @XmlElement
    @Min(1)
    private double engine;

    public CarImportDTO() {
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }
}
