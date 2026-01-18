package br.com.jovirds.repository;

import br.com.jovirds.integrationtest.testcontainers.AbstractIntegrationTest;
import br.com.jovirds.model.Game;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    GameRepository repository;
    private static Game game;

    @BeforeAll
    static void setUp(){
        game = new Game();
    }

    @Test
    @Order(1)
    void findGamesByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "name","id"));
        game = repository.findGamesByName("Cyberpunk", pageable).getContent().get(0);

        assertNotNull(game);
        assertNotNull(game.getId());
        assertEquals("Cyberpunk 2077", game.getName());
        assertEquals("CD Projekt Red", game.getDeveloper());
        assertTrue(game.getFinished());
        assertEquals(2020L, game.getYear());
    }

    @Test
    @Order(2)
    void finishGame() {

        Long id = game.getId();
        repository.finishGame(id);

        var result = repository.findById(id);
        game = result.get();

        assertNotNull(game);
        assertNotNull(game.getId());
        assertEquals("CD Projekt Red", game.getDeveloper());
        assertEquals("Cyberpunk 2077", game.getName());
        assertTrue(game.getFinished());
        assertEquals(2020L, game.getYear());

    }
}