package br.com.jovirds.data.dto.V2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({
        "id",
        "name",
        "star_rating",
        "developer",
        "year",
        "finished"
})
public class GameDTOV2 extends RepresentationModel<GameDTOV2> implements Serializable {

    private Long id;
    private String name;
    private String developer;
    private Long year;
    private Boolean finished;

    @JsonProperty("star_rating")
    private Long starRating;


    public GameDTOV2(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getStarRating() { return starRating; }

    public void setStarRating(Long starRating) { this.starRating = starRating; }

    public Boolean getFinished() { return finished; }

    public void setFinished(Boolean finished) { this.finished = finished; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameDTOV2 gameDTOV2 = (GameDTOV2) o;
        return Objects.equals(id, gameDTOV2.id) && Objects.equals(name, gameDTOV2.name) && Objects.equals(developer, gameDTOV2.developer) && Objects.equals(year, gameDTOV2.year) && Objects.equals(finished, gameDTOV2.finished) && Objects.equals(starRating, gameDTOV2.starRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, developer, year, finished, starRating);
    }
}
