package nkhatun.demo.harrykart.java.service;

import org.springframework.http.ResponseEntity;

import nkhatun.demo.harrykart.java.dto.RankingHolder;
import nkhatun.demo.harrykart.java.dto.RequestDto;

public interface HarryKartService {
	
	public ResponseEntity<RankingHolder> applyPower(RequestDto requestDto);
}
