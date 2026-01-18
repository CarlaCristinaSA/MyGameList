package br.com.jovirds.integrationtest.controllers.game.withxml;

import br.com.jovirds.configs.TestConfigs;
import br.com.jovirds.integrationtest.dto.GameDTO;
import br.com.jovirds.integrationtest.dto.wrappers.xml.PagedModelGame;
import br.com.jovirds.integrationtest.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static GameDTO game;

    @BeforeAll()
    static void setUp() {

        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockGame();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_8080)
                    .setBasePath("/api/game/v1")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .body(game)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 29", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(!createGame.getFinished());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        game.setName("Ritoreio 30");

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .body(game)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 30", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(!createGame.getFinished());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", game.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 30", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(!createGame.getFinished());
    }

    @Test
    @Order(4)
    void finishedIdTest() throws JsonProcessingException {

        var content  = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", game.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        GameDTO createGame = objectMapper.readValue(content, GameDTO.class);
        game = createGame;

        assertNotNull(createGame.getId());
        assertTrue(createGame.getId() > 0);

        assertEquals("Ritoreio 30", createGame.getName());
        assertEquals("Developer dto", createGame.getDeveloper());
        assertEquals(2000L, createGame.getYear());
        assertTrue(createGame.getFinished());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", game.getId())
                    .when()
                        .delete("{id}")
                    .then()
                        .statusCode(204);

    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content  = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParam("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelGame wrapper = objectMapper.readValue(content, PagedModelGame.class);
        List<GameDTO> games = wrapper.getContent();

        GameDTO createOne = games.get(0);
        game = createOne;

        assertNotNull(createOne.getId());
        assertTrue(createOne.getId() > 0);

        assertEquals("Cyberpunk 2077", createOne.getName());
        assertEquals("CD Projekt Red", createOne.getDeveloper());
        assertEquals(2020L, createOne.getYear());
        assertTrue(createOne.getFinished());
    }

    private void mockGame() {
        game = new GameDTO();
        game.setName("Ritoreio 29");
        game.setDeveloper("Developer dto");
        game.setYear(2000L);
        game.setFinished(false);
    }

}