package br.com.jovirds.integrationtest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
@JsonPropertyOrder({
        "id",
        "name",
        "developer",
        "year"
    })
//@JsonFilter("GameFilter")
public class GameDTO implements Serializable {

    private Long id;
    private String name;
    private String developer;
    private Long year;
    private Boolean finished;

    public GameDTO(){};

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

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO gameDTO = (GameDTO) o;
        return Objects.equals(id, gameDTO.id) && Objects.equals(name, gameDTO.name) && Objects.equals(developer, gameDTO.developer) && Objects.equals(year, gameDTO.year) && Objects.equals(finished, gameDTO.finished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, developer, year, finished);
    }
}
