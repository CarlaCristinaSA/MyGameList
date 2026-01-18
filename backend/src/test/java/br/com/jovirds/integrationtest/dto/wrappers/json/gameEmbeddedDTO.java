package br.com.jovirds.integrationtest.dto.wrappers.json;

import br.com.jovirds.integrationtest.dto.GameDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class gameEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("games")
    private List<GameDTO> games;

    public gameEmbeddedDTO() {}

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }
}
