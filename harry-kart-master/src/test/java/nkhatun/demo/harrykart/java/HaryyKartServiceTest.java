package nkhatun.demo.harrykart.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import nkhatun.demo.harrykart.java.dto.RankingDto;
import nkhatun.demo.harrykart.java.dto.RankingHolder;
import nkhatun.demo.harrykart.java.dto.RequestDto;
import nkhatun.demo.harrykart.java.service.HarryKartService;
@SpringBootTest
@ExtendWith(SpringExtension.class)
class HaryyKartServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(HaryyKartServiceTest.class.getName());
	
	@Autowired
	HarryKartService harryKartService;
	private static final  String inputXmlStringValid = "<harryKart><numberOfLoops>3</numberOfLoops><startList><participant><lane>1</lane><name>TIMETOBELUCKY</name><baseSpeed>10</baseSpeed></participant><participant><lane>2</lane><name>CARGO DOOR</name><baseSpeed>10</baseSpeed></participant><participant><lane>3</lane><name>HERCULES BOKO</name><baseSpeed>10</baseSpeed></participant><participant><lane>4</lane><name>WAIKIKI SILVIO</name><baseSpeed>10</baseSpeed></participant></startList><powerUps><loop number=\"1\"><lane number=\"1\">0</lane><lane number=\"2\">0</lane><lane number=\"3\">1</lane><lane number=\"4\">3</lane></loop><loop number=\"2\"><lane number=\"1\">10</lane><lane number=\"2\">0</lane><lane number=\"3\">0</lane><lane number=\"4\">1</lane></loop></powerUps></harryKart>";
	private static final  String inputXmlStringBaseNegative = "<harryKart><numberOfLoops>3</numberOfLoops><startList><participant><lane>1</lane><name>TIMETOBELUCKY</name><baseSpeed>-10</baseSpeed></participant><participant><lane>2</lane><name>CARGO DOOR</name><baseSpeed>10</baseSpeed></participant><participant><lane>3</lane><name>HERCULES BOKO</name><baseSpeed>10</baseSpeed></participant><participant><lane>4</lane><name>WAIKIKI SILVIO</name><baseSpeed>10</baseSpeed></participant></startList><powerUps><loop number=\"1\"><lane number=\"1\">0</lane><lane number=\"2\">0</lane><lane number=\"3\">1</lane><lane number=\"4\">3</lane></loop><loop number=\"2\"><lane number=\"1\">10</lane><lane number=\"2\">0</lane><lane number=\"3\">0</lane><lane number=\"4\">1</lane></loop></powerUps></harryKart>";
	private static final  String inputXmlStringPowerUpseNegative = "<harryKart><numberOfLoops>3</numberOfLoops><startList><participant><lane>1</lane><name>TIMETOBELUCKY</name><baseSpeed>10</baseSpeed></participant><participant><lane>2</lane><name>CARGO DOOR</name><baseSpeed>10</baseSpeed></participant><participant><lane>3</lane><name>HERCULES BOKO</name><baseSpeed>10</baseSpeed></participant><participant><lane>4</lane><name>WAIKIKI SILVIO</name><baseSpeed>10</baseSpeed></participant></startList><powerUps><loop number=\"1\"><lane number=\"1\">0</lane><lane number=\"2\">0</lane><lane number=\"3\">1</lane><lane number=\"4\">-30</lane></loop><loop number=\"2\"><lane number=\"1\">10</lane><lane number=\"2\">0</lane><lane number=\"3\">0</lane><lane number=\"4\">1</lane></loop></powerUps></harryKart>";
	private static final  String inputXmlStringInvalidLoop ="<harryKart><numberOfLoops>5</numberOfLoops><startList><participant><lane>1</lane><name>TIMETOBELUCKY</name><baseSpeed>10</baseSpeed></participant><participant><lane>2</lane><name>CARGO DOOR</name><baseSpeed>10</baseSpeed></participant><participant><lane>3</lane><name>HERCULES BOKO</name><baseSpeed>10</baseSpeed></participant><participant><lane>4</lane><name>WAIKIKI SILVIO</name><baseSpeed>10</baseSpeed></participant></startList><powerUps><loop number=\"1\"><lane number=\"1\">0</lane><lane number=\"2\">0</lane><lane number=\"3\">1</lane><lane number=\"4\">-30</lane></loop><loop number=\"2\"><lane number=\"1\">10</lane><lane number=\"2\">0</lane><lane number=\"3\">0</lane><lane number=\"4\">1</lane></loop></powerUps></harryKart>";
	private static final  String inputXmlStringSingleLoop = "<harryKart><numberOfLoops>3</numberOfLoops><startList><participant><lane>1</lane><name>TIMETOBELUCKY</name><baseSpeed>10</baseSpeed></participant><participant><lane>2</lane><name>CARGO DOOR</name><baseSpeed>20</baseSpeed></participant><participant><lane>3</lane><name>HERCULES BOKO</name><baseSpeed>30</baseSpeed></participant><participant><lane>4</lane><name>WAIKIKI SILVIO</name><baseSpeed>40</baseSpeed></participant></startList><powerUps><loop number=\"1\"><lane number=\"1\">0</lane><lane number=\"2\">0</lane><lane number=\"3\">1</lane><lane number=\"4\">-30</lane></loop><loop number=\"2\"><lane number=\"1\">10</lane><lane number=\"2\">0</lane><lane number=\"3\">0</lane><lane number=\"4\">1</lane></loop></powerUps></harryKart>";
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testApplyPowerUps() {
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(buildRequestDto(inputXmlStringValid));
		assertEquals(HttpStatus.OK,	rankingHolderResponse.getStatusCode());
		RankingHolder holder = rankingHolderResponse.getBody();
		List<RankingDto> rankingDtoList = holder.getRanking();
		assertEquals(BigInteger.valueOf(3).intValue(),rankingDtoList.size());
		
		for (int i = 1; i < rankingDtoList.size() + 1; i++) {
			RankingDto rankingDto = rankingDtoList.get(i - 1);
			switch (i) {
				case 1 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("WAIKIKI SILVIO");
					assertThat(rankingDto.getPosition()).isEqualTo(1);
					break;
				case 2 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("TIMETOBELUCKY");
					assertThat(rankingDto.getPosition()).isEqualTo(2);
					break;
				case 3 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("HERCULES BOKO");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;
	}
	
	@Test
	void testApplyPowerUpsBaseNgative() {
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(buildRequestDto(inputXmlStringBaseNegative));
		assertEquals(HttpStatus.OK,	rankingHolderResponse.getStatusCode());
		RankingHolder holder = rankingHolderResponse.getBody();
		List<RankingDto> rankingDtoList = holder.getRanking();
		assertEquals(BigInteger.valueOf(3).intValue(),rankingDtoList.size());
		
		for (int i = 1; i < rankingDtoList.size() + 1; i++) {
			RankingDto rankingDto = rankingDtoList.get(i - 1);
			switch (i) {
				case 1 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("WAIKIKI SILVIO");
					assertThat(rankingDto.getPosition()).isEqualTo(1);
					break;
				case 2 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("HERCULES BOKO");
					assertThat(rankingDto.getPosition()).isEqualTo(2);
					break;
				case 3 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("CARGO DOOR");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;
	}
	
	@Test
	void testApplyPowerUpsNgative() {
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(buildRequestDto(inputXmlStringPowerUpseNegative));
		assertEquals(HttpStatus.OK,	rankingHolderResponse.getStatusCode());
		RankingHolder holder = rankingHolderResponse.getBody();
		List<RankingDto> rankingDtoList = holder.getRanking();
		assertEquals(BigInteger.valueOf(3).intValue(),rankingDtoList.size());
		
		for (int i = 1; i < rankingDtoList.size() + 1; i++) {
			RankingDto rankingDto = rankingDtoList.get(i - 1);
			switch (i) {
				case 1 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("TIMETOBELUCKY");
					assertThat(rankingDto.getPosition()).isEqualTo(1);
					break;
				case 2 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("HERCULES BOKO");
					assertThat(rankingDto.getPosition()).isEqualTo(2);
					break;
				case 3 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("CARGO DOOR");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;
	}
	
	@Test
	void testInvalidLoopCount() {
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(buildRequestDto(inputXmlStringInvalidLoop));
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,	rankingHolderResponse.getStatusCode());
	}

	@Test
	void testZeroLoopCount() {
		RequestDto requestDto = buildRequestDto(inputXmlStringInvalidLoop);
		requestDto.setNumberOfLoops(BigInteger.ZERO);
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(requestDto);
		assertEquals(HttpStatus.NO_CONTENT,	rankingHolderResponse.getStatusCode());
	}
	@Test
	void testSingleLoopCount() {
		ResponseEntity<RankingHolder> rankingHolderResponse = harryKartService.applyPower(buildRequestDto(inputXmlStringSingleLoop));
		assertEquals(HttpStatus.OK,	rankingHolderResponse.getStatusCode());
		RankingHolder holder = rankingHolderResponse.getBody();
		List<RankingDto> rankingDtoList = holder.getRanking();
		assertEquals(BigInteger.valueOf(3).intValue(),rankingDtoList.size());
		
		for (int i = 1; i < rankingDtoList.size() + 1; i++) {
			RankingDto rankingDto = rankingDtoList.get(i - 1);
			switch (i) {
				case 1 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("HERCULES BOKO");
					assertThat(rankingDto.getPosition()).isEqualTo(1);
					break;
				case 2 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("CARGO DOOR");
					assertThat(rankingDto.getPosition()).isEqualTo(2);
					break;
				case 3 :
					assertThat(rankingDto.getHorse())
							.isEqualTo("TIMETOBELUCKY");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;
	}

	private RequestDto buildRequestDto(String inputXmlString) {
		RequestDto requestDto = new RequestDto();
		 XmlMapper xmlMapper = new XmlMapper();
		 try {
			requestDto = xmlMapper.readValue(inputXmlString, RequestDto.class);
		} catch (JsonMappingException e) {
			logger.info("Exception occured while parsing xml file: "+e.getMessage());
		} catch (JsonProcessingException e) {
			logger.info("Exception occured while parsing xml file: "+e.getMessage());
		}
		return requestDto;
	}

}
