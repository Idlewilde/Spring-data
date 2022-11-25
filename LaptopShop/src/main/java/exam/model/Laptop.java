package exam.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "laptops")

public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "mac_address")
    private String macAddress;

    @Column(nullable = false, name = "cpu_speed")
    private double cpuSpeed;

    @Column(nullable = false)
    private int ram;

    @Column(nullable = false)
    private int storage;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name = "warranty_type")
    private WarrantyType warrantyType;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Override
    public String toString() {
        return String.format("Laptop - %s\n" +
                        "*Cpu speed - %.2f\n" +
                        "**Ram - %d\n" +
                        "***Storage - %d\n" +
                        "****Price - %.2f\n" +
                        "#Shop name - %s\n" +
                        "##Town - %s\n", this.macAddress, this.cpuSpeed,
                this.ram, this.storage, this.price,
                this.shop.getName(), this.shop.getTown().getName());
    }
}
