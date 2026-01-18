package br.com.jovirds.unitests.mocks;

import br.com.jovirds.model.Game;
import br.com.jovirds.data.dto.V1.GameDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MockGame {

    public Game mockEntity() {
        return mockEntity(0);
    }

    public GameDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Game> mockEntityList() {
        List<Game> games = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            games.add(mockEntity(i));
        }
        return games;
    }

    public List<GameDTO> mockDTOList() {
        List<GameDTO> games = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            games.add(mockDTO(i));
        }
        return games;
    }

    public Game mockEntity(Integer number) {
        Game game = new Game();
        game.setId(number.longValue());
        game.setName("Game Test " + number);
        game.setDeveloper((number % 2 == 0) ? "Developer Even" : "Developer Odd");
        game.setYear(2000L + number); // só para variar os anos
        return game;
    }

    public GameDTO mockDTO(Integer number) {
        GameDTO game = new GameDTO();
        game.setId(number.longValue());
        game.setName("Game Test " + number);
        game.setDeveloper((number % 2 == 0) ? "Developer Even" : "Developer Odd");
        game.setYear(2000L + number);
        return game;
    }
}
