package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class OfferImportDTO {
    @Positive
    @XmlElement(name="price")
    private BigDecimal price;
    @XmlElement(name="agent")
    private AgentXMLImportDTO agent;
    @XmlElement(name="apartment")
    private ApartmentXMLImportDTO apartment;
    @XmlElement(name="publishedOn")
    private String publishedOn;


    public OfferImportDTO() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AgentXMLImportDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentXMLImportDTO agent) {
        this.agent = agent;
    }

    public ApartmentXMLImportDTO getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentXMLImportDTO apartment) {
        this.apartment = apartment;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }
}
