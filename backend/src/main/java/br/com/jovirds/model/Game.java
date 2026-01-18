package br.com.jovirds.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final AtomicLong counter = new AtomicLong();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 80)
    private String developer;

    @Column(nullable = false)
    private Long year;

    @Column(name = "star_rating", nullable = true)
    private Long starRating;

    @Column(nullable = false)
    private Boolean finished;

    public Game(){};

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

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(name, game.name) && Objects.equals(developer, game.developer) && Objects.equals(year, game.year) && Objects.equals(starRating, game.starRating) && Objects.equals(finished, game.finished);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, developer, year, starRating, finished);
    }
}
