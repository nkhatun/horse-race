package nkhatun.demo.harrykart.java.rest;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nkhatun.demo.harrykart.java.dto.RankingHolder;
import nkhatun.demo.harrykart.java.dto.RequestDto;
import nkhatun.demo.harrykart.java.service.HarryKartService;

@RestController
@RequestMapping("/java/api")
public class HarryKartController {
	Logger log = Logger.getLogger(HarryKartController.class.getName());
	@Autowired
	HarryKartService harrykartService;

    @PostMapping(path = "/play", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RankingHolder> playHarryKart(@Validated @RequestBody RequestDto requestDto) {
    	log.info("Logging request :: "+requestDto);
    	return harrykartService.applyPower(requestDto);
    }

}
