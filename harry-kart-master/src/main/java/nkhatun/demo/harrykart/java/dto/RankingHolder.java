package nkhatun.demo.harrykart.java.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingHolder {
	private List<RankingDto> ranking;
}
