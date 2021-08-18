package nkhatun.demo.harrykart.java.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoopDto implements Serializable {
	private BigInteger number;
	private List<LaneDto> lane;
    @JacksonXmlElementWrapper(localName = "lane", useWrapping = false)
	@JacksonXmlProperty(localName = "lane")
    public List<LaneDto> getLane() {
        return this.lane;
    }
	    @JacksonXmlProperty(isAttribute = true)
		public BigInteger getNumber() {
			return this.number;
		}
}
