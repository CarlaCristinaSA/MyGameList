package br.com.jovirds.integrationtest.controllers.game.cors.withjson;

import br.com.jovirds.configs.TestConfigs;
import br.com.jovirds.integrationtest.dto.GameDTO;
import br.com.jovirds.integrationtest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static GameDTO game;

    @BeforeAll()
    static void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockGame();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_8080)
                    .setBasePath("/api/game/v1")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(game)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertNotNull(createGame.getName());
        assertNotNull(createGame.getDeveloper());
        assertNotNull(createGame.getYear());

        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 29", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(createGame.getFinished());

    }

    @Test
    @Order(2)
    void createWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_4000)
                .setBasePath("/game")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(game)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertEquals("Invalid CORS request", content);

    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_8080)
                .setBasePath("/api/game/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", game.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertNotNull(createGame.getName());
        assertNotNull(createGame.getDeveloper());
        assertNotNull(createGame.getYear());

        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 29", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(createGame.getFinished());
    }

    private void mockGame() {
        game = new GameDTO();
        game.setName("Ritoreio 29");
        game.setDeveloper("Developer dto");
        game.setYear(2000L);
        game.setFinished(true);
    }

}