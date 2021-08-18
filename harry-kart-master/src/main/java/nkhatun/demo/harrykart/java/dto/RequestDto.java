package nkhatun.demo.harrykart.java.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
@Data
@JacksonXmlRootElement(localName = "harryKart")
public class RequestDto implements Serializable{
	private BigInteger numberOfLoops;
	private List<ParticipantDto> startList;
	private List<LoopDto> powerUps;
	@XmlElement(name="powerUps")
    @JacksonXmlProperty(isAttribute = true)
    public List<LoopDto> getPowerUps() {
        return this.powerUps;
    }


}
