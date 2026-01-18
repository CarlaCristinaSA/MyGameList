package br.com.jovirds.unitests.mapper;

import static br.com.jovirds.mapper.ObjectMapper.parseObject;
import static br.com.jovirds.mapper.ObjectMapper.parseListObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.model.Game;
import br.com.jovirds.unitests.mocks.MockGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectMapperTests {

    MockGame inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockGame();
    }

    @Test
    public void parseEntityToDTOTest() {
        GameDTO output = parseObject(inputObject.mockEntity(), GameDTO.class);

        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Game Test 0", output.getName());
        assertEquals("Developer Even", output.getDeveloper());
        assertEquals(Long.valueOf(2000L), output.getYear());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<GameDTO> outputList = parseListObject(inputObject.mockEntityList(), GameDTO.class);

        GameDTO outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Game Test 0", outputZero.getName());
        assertEquals("Developer Even", outputZero.getDeveloper());
        assertEquals(Long.valueOf(2000L), outputZero.getYear());

        GameDTO outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Game Test 7", outputSeven.getName());
        assertEquals("Developer Odd", outputSeven.getDeveloper());
        assertEquals(Long.valueOf(2007L), outputSeven.getYear());

        GameDTO outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Game Test 12", outputTwelve.getName());
        assertEquals("Developer Even", outputTwelve.getDeveloper());
        assertEquals(Long.valueOf(2012L), outputTwelve.getYear());
    }

    @Test
    public void parseDTOToEntityTest() {
        Game output = parseObject(inputObject.mockDTO(), Game.class);

        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Game Test 0", output.getName());
        assertEquals("Developer Even", output.getDeveloper());
        assertEquals(Long.valueOf(2000L), output.getYear());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Game> outputList = parseListObject(inputObject.mockDTOList(), Game.class);

        Game outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Game Test 0", outputZero.getName());
        assertEquals("Developer Even", outputZero.getDeveloper());
        assertEquals(Long.valueOf(2000L), outputZero.getYear());

        Game outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Game Test 7", outputSeven.getName());
        assertEquals("Developer Odd", outputSeven.getDeveloper());
        assertEquals(Long.valueOf(2007L), outputSeven.getYear());

        Game outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Game Test 12", outputTwelve.getName());
        assertEquals("Developer Even", outputTwelve.getDeveloper());
        assertEquals(Long.valueOf(2012L), outputTwelve.getYear());
    }
}
