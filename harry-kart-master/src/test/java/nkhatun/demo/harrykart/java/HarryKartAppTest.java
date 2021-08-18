package nkhatun.demo.harrykart.java;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import nkhatun.demo.harrykart.java.dto.RankingDto;
import nkhatun.demo.harrykart.java.dto.RankingHolder;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("java-test")
public class HarryKartAppTest {

	private final static URI harryKartApp = URI
			.create("http://localhost:8181/java/api/play");

	@BeforeAll
	void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	@Test
	@DisplayName("Trying to GET instead of POST should return 405 Method not allowed")
	void useGetOnPostEndpointShouldNotBePossible() {
		given().auth().basic("demo", "demo").when().get(harryKartApp).then()
				.assertThat().statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
	}

	@Test
	@DisplayName("The application returns the exact ranking json")
	void testValidResponse() {
		String inputXml = "<harryKart>\n"
				+ "    <numberOfLoops>3</numberOfLoops>\n" + "    <startList>\n"
				+ "        <participant>\n" + "            <lane>1</lane>\n"
				+ "            <name>TIMETOBELUCKY</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>2</lane>\n"
				+ "            <name>CARGO DOOR</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>3</lane>\n"
				+ "            <name>HERCULES BOKO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>4</lane>\n"
				+ "            <name>WAIKIKI SILVIO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "    </startList>\n"
				+ "    <powerUps>\n" + "        <loop number=\"1\">\n"
				+ "            <lane number=\"1\">0</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">1</lane>\n"
				+ "            <lane number=\"4\">3</lane>\n"
				+ "        </loop>\n" + "        <loop number=\"2\">\n"
				+ "            <lane number=\"1\">10</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">0</lane>\n"
				+ "            <lane number=\"4\">1</lane>\n"
				+ "        </loop>\n" + "    </powerUps>\n" + "</harryKart>";
		RankingHolder holder = given().auth().basic("demo", "demo")
				.header("Content-Type", ContentType.XML).body(inputXml).when()
				.post(harryKartApp).then().assertThat()
				.statusCode(HttpStatus.OK.value()).and()
				.header("Content-Type", ContentType.JSON.toString()).and()
				.extract().as(RankingHolder.class);
		assertThat(holder.getRanking().size()).isEqualTo(3);
		List<RankingDto> rankingDtoList = holder.getRanking();
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
	@DisplayName("The participant is out of race with a negative base speed")
	void testOutOfRaceIfBaseSpeedIsNegative() {
		String inputXml = "<harryKart>\n"
				+ "    <numberOfLoops>3</numberOfLoops>\n" + "    <startList>\n"
				+ "        <participant>\n" + "            <lane>1</lane>\n"
				+ "            <name>TIMETOBELUCKY</name>\n"
				+ "            <baseSpeed>-10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>2</lane>\n"
				+ "            <name>CARGO DOOR</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>3</lane>\n"
				+ "            <name>HERCULES BOKO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>4</lane>\n"
				+ "            <name>WAIKIKI SILVIO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "    </startList>\n"
				+ "    <powerUps>\n" + "        <loop number=\"1\">\n"
				+ "            <lane number=\"1\">0</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">1</lane>\n"
				+ "            <lane number=\"4\">3</lane>\n"
				+ "        </loop>\n" + "        <loop number=\"2\">\n"
				+ "            <lane number=\"1\">10</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">0</lane>\n"
				+ "            <lane number=\"4\">1</lane>\n"
				+ "        </loop>\n" + "    </powerUps>\n" + "</harryKart>";
		RankingHolder holder = given().auth().basic("demo", "demo")
				.header("Content-Type", ContentType.XML).body(inputXml).when()
				.post(harryKartApp).then().assertThat()
				.statusCode(HttpStatus.OK.value()).and()
				.header("Content-Type", ContentType.JSON.toString()).and()
				.extract().as(RankingHolder.class);
		assertThat(holder.getRanking().size()).isEqualTo(3);
		List<RankingDto> rankingDtoList = holder.getRanking();
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
					assertThat(rankingDto.getHorse()).isEqualTo("CARGO DOOR");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;

	}
	
	@Test
	@DisplayName("to test if the no of loops in request matches with the loop list in power ups")
	void testIfNoOfLoopDoesntMatch() {
		String inputXml = "<harryKart>\n"
				+ "    <numberOfLoops>4</numberOfLoops>\n" + "    <startList>\n"
				+ "        <participant>\n" + "            <lane>1</lane>\n"
				+ "            <name>TIMETOBELUCKY</name>\n"
				+ "            <baseSpeed>-10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>2</lane>\n"
				+ "            <name>CARGO DOOR</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>3</lane>\n"
				+ "            <name>HERCULES BOKO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "        <participant>\n"
				+ "            <lane>4</lane>\n"
				+ "            <name>WAIKIKI SILVIO</name>\n"
				+ "            <baseSpeed>10</baseSpeed>\n"
				+ "        </participant>\n" + "    </startList>\n"
				+ "    <powerUps>\n" + "        <loop number=\"1\">\n"
				+ "            <lane number=\"1\">0</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">1</lane>\n"
				+ "            <lane number=\"4\">3</lane>\n"
				+ "        </loop>\n" + "        <loop number=\"2\">\n"
				+ "            <lane number=\"1\">10</lane>\n"
				+ "            <lane number=\"2\">0</lane>\n"
				+ "            <lane number=\"3\">0</lane>\n"
				+ "            <lane number=\"4\">1</lane>\n"
				+ "        </loop>\n" + "    </powerUps>\n" + "</harryKart>";
		 given().auth().basic("demo", "demo")
				.header("Content-Type", ContentType.XML).body(inputXml).when()
				.post(harryKartApp).then().assertThat()
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

	}
	
	@Test
	@DisplayName("to test if the power ups are negative to make the participant out of race")
	void testIfNegativePowerUps() {
		String inputXml = "<harryKart>\n" + 
				"    <numberOfLoops>3</numberOfLoops>\n" + 
				"    <startList>\n" + 
				"        <participant>\n" + 
				"            <lane>1</lane>\n" + 
				"            <name>TIMETOBELUCKY</name>\n" + 
				"            <baseSpeed>10</baseSpeed>\n" + 
				"        </participant>\n" + 
				"        <participant>\n" + 
				"            <lane>2</lane>\n" + 
				"            <name>CARGO DOOR</name>\n" + 
				"            <baseSpeed>10</baseSpeed>\n" + 
				"        </participant>\n" + 
				"        <participant>\n" + 
				"            <lane>3</lane>\n" + 
				"            <name>HERCULES BOKO</name>\n" + 
				"            <baseSpeed>10</baseSpeed>\n" + 
				"        </participant>\n" + 
				"        <participant>\n" + 
				"            <lane>4</lane>\n" + 
				"            <name>WAIKIKI SILVIO</name>\n" + 
				"            <baseSpeed>10</baseSpeed>\n" + 
				"        </participant>\n" + 
				"    </startList>\n" + 
				"    <powerUps>\n" + 
				"        <loop number=\"1\">\n" + 
				"            <lane number=\"1\">0</lane>\n" + 
				"            <lane number=\"2\">0</lane>\n" + 
				"            <lane number=\"3\">1</lane>\n" + 
				"            <lane number=\"4\">-30</lane>\n" + 
				"        </loop>\n" + 
				"        <loop number=\"2\">\n" + 
				"            <lane number=\"1\">10</lane>\n" + 
				"            <lane number=\"2\">0</lane>\n" + 
				"            <lane number=\"3\">0</lane>\n" + 
				"            <lane number=\"4\">1</lane>\n" + 
				"        </loop>\n" + 
				"    </powerUps>\n" + 
				"</harryKart>";
		RankingHolder holder = given().auth().basic("demo", "demo")
				.header("Content-Type", ContentType.XML).body(inputXml).when()
				.post(harryKartApp).then().assertThat()
				.statusCode(HttpStatus.OK.value()).and()
				.header("Content-Type", ContentType.JSON.toString()).and()
				.extract().as(RankingHolder.class);
		assertThat(holder.getRanking().size()).isEqualTo(3);
		List<RankingDto> rankingDtoList = holder.getRanking();
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
					assertThat(rankingDto.getHorse()).isEqualTo("CARGO DOOR");
					assertThat(rankingDto.getPosition()).isEqualTo(3);
					break;
				default :
					break;
			}

		} ;

	}
}
