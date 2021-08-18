package nkhatun.demo.harrykart.java.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

@Data
public class PowerUpsDto {
	@XmlElement(name = "loop")
	private List<LoopDto> loop;
	
}
