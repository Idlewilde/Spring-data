package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class TownImportDTO {

    @Size(min = 2)
    private String townName;
    @Positive
    private int population;

    public TownImportDTO() {
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
