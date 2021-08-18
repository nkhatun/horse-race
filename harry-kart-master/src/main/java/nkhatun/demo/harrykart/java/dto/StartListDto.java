package nkhatun.demo.harrykart.java.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConstructorBinding;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class StartListDto implements Serializable{
	private List<ParticipantDto> participant;
	@JsonCreator
	public StartListDto(@JsonProperty("participant") List<ParticipantDto> participant) {
	    this.participant = participant;
	}
}
