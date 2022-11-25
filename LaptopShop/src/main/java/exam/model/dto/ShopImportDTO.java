package exam.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopImportDTO {

    @XmlElement
    @Size(min = 4)
    private String name;

    @XmlElement
    @Min(20000)
    private BigDecimal income;

    @XmlElement
    @Size(min = 4)
    private String address;

    @XmlElement(name = "employee-count")
    @Min(1)
    @Max(50)
    private int employeeCount;

    @XmlElement(name = "shop-area")
    @Min(150)
    private int shopArea;

    @XmlElement(name = "town")
    private TownNameXMLDTO town;
}
