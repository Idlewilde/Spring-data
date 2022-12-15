package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;


    @ManyToOne
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public String toString() {
        String row1 = String.format("Car %s %s with %dkm",
                this.car.getMake(),
                this.car.getModel(),
                this.car.getKilometers());
        String row2 = String.format("-Mechanic: %s %s - task №%d:",
                this.mechanic.getFirstName(),
                this.mechanic.getLastName(),
                this.id);
        String row3 = String.format("--Engine: %.1f", this.car.getEngine());
        String row4 = String.format("---Price: %.2f$", this.price);
        return String.format("%s\n%s\n%s\n%s", row1, row2, row3, row4);
    }
}
