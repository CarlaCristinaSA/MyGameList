package br.com.jovirds.unitests.service;

import br.com.jovirds.data.dto.V1.GameDTO;
import br.com.jovirds.exception.RequiredObjectIsNullException;
import br.com.jovirds.model.Game;
import br.com.jovirds.repository.GameRepository;
import br.com.jovirds.service.GameService;
import br.com.jovirds.unitests.mocks.MockGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    MockGame input;

    @InjectMocks
    private GameService service;

    @Mock
    GameRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockGame();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Game game = input.mockEntity(1);
        game.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(game));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("GET")
                )
        );

        assertEquals("Developer Odd", result.getDeveloper());
        assertEquals("Game Test 1", result.getName());
        assertEquals(2001 , result.getYear());

    }

    @Test
    void create() {

        Game game = input.mockEntity(1);
        game.setId(1L);
        Game persisted = game;

        GameDTO dto = input.mockDTO(1);
        when(repository.save(game)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("GET")
                )
        );

        assertEquals("Developer Odd", result.getDeveloper());
        assertEquals("Game Test 1", result.getName());
        assertEquals(2001 , result.getYear());

    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {

        Game game = input.mockEntity(1);
        Game persisted = game;
        persisted.setId(1L);

        GameDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(game));
        when(repository.save(game)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("GET")
                )
        );

        assertEquals("Developer Odd", result.getDeveloper());
        assertEquals("Game Test 1", result.getName());
        assertEquals(2001 , result.getYear());

    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {

        Game game = input.mockEntity(1);
        game.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(game));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Game.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("Not terminated functionu")
    void findAll() {
        List<Game> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<GameDTO> games = new ArrayList<>();

        assertNotNull(games);
        assertEquals(14, games.size());

        var gameOne = games.get(1);

        assertNotNull(gameOne);
        assertNotNull(gameOne.getId());
        assertNotNull(gameOne.getLinks());

        assertNotNull(gameOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(gameOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(gameOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/game/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertNotNull(gameOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(gameOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("GET")
                )
        );

        assertEquals("Developer Odd", gameOne.getDeveloper());
        assertEquals("Game Test 1", gameOne.getName());
        assertEquals(2001 , gameOne.getYear());

        var gameFour = games.get(4);

        assertNotNull(gameFour);
        assertNotNull(gameFour.getId());
        assertNotNull(gameFour.getLinks());

        assertNotNull(gameFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/game/4")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(gameFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(gameFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/game/4")
                        && link.getType().equals("DELETE")
                )
        );

        assertNotNull(gameFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(gameFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/game")
                        && link.getType().equals("GET")
                )
        );

        assertEquals("Developer Even", gameFour.getDeveloper());
        assertEquals("Game Test 4", gameFour.getName());
        assertEquals(2004 , gameFour.getYear());

    }
}