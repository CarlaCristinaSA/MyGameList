package br.com.jovirds.mapper.custom;

import br.com.jovirds.data.dto.V2.GameDTOV2;
import br.com.jovirds.model.Game;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    public GameDTOV2 convertEntityToDTOV2(Game game) {
        GameDTOV2 dto = new GameDTOV2();
        dto.setYear(game.getYear());
        dto.setName(game.getName());
        dto.setId(game.getId());
        dto.setDeveloper(game.getDeveloper());
        dto.setStarRating(game.getStarRating());
        dto.setFinished(game.getFinished());
        return dto;
    }

    public Game convertDTOV2ToEntity(GameDTOV2 game) {
        Game entity = new Game();
        entity.setYear(game.getYear());
        entity.setName(game.getName());
        entity.setId(game.getId());
        entity.setDeveloper(game.getDeveloper());
        entity.setStarRating(game.getStarRating());
        entity.setFinished(game.getFinished());

        return entity;
    }


}
