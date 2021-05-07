package com.deviget.minesweeperapi.controller.api;

import com.deviget.minesweeperapi.controller.request.GameRequest;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.domain.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.deviget.minesweeperapi.domain.model.GameStatus.CREATED;
import static com.deviget.minesweeperapi.domain.model.GameStatus.PAUSED;
import static com.deviget.minesweeperapi.domain.model.GameStatus.PLAYING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Create game is successful and the initial status is created")
    void testCreateGameSuccessfully() throws Exception {
        GameRequest gameRequest = new GameRequest("userTest", 10, 10, 5);
        Mockito.when(gameService.createGame(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Game.builder().status(CREATED).build());

        String gameRequestAsString = objectMapper.writeValueAsString(gameRequest);
        MvcResult result = mockMvc.perform(post("/v1/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameRequestAsString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.game.status").value(CREATED.name()))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Get all games by userId is successful")
    void testGetGamesSuccessfully() throws Exception {
        Game playingGame = Game.builder().status(PLAYING).id(111).build();
        Game pausedGame = Game.builder().status(PAUSED).id(112).build();
        Mockito.when(gameService.getGamesByUser(eq("user_test")))
                .thenReturn(List.of(playingGame, pausedGame));

        MvcResult result = mockMvc.perform(get("/v1/users/user_test/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.games", hasSize(2)))
                .andExpect(jsonPath("$.games[0].id").value(111))
                .andExpect(jsonPath("$.games[0].status").value(PLAYING.name()))
                .andExpect(jsonPath("$.games[1].id").value(112))
                .andExpect(jsonPath("$.games[1].status").value(PAUSED.name()))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Get game by id is successful")
    void testGetGameByIdSuccessfully() throws Exception {
        Game game = Game.builder().status(PLAYING).id(111).build();
        Mockito.when(gameService.getGameById(eq(111)))
                .thenReturn(game);

        MvcResult result = mockMvc.perform(get("/v1/games/111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.id").value(111))
                .andExpect(jsonPath("$.game.status").value(PLAYING.name()))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Pause game successfully")
    void testPauseGameSuccessfully() throws Exception {
        Mockito.when(gameService.pauseGame(eq(111)))
                .thenReturn(true);

        MvcResult result = mockMvc.perform(patch("/v1/games/111/pause"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Play the game successfully")
    void testPlayGameSuccessfully() throws Exception {
        Mockito.when(gameService.playGame(eq(111)))
                .thenReturn(true);

        MvcResult result = mockMvc.perform(patch("/v1/games/111/play"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Reveal a cell successfully while playing the game")
    void testRevealCellSuccessfully() throws Exception {
        Game game = Game.builder().status(PLAYING).id(111).build();
        Mockito.when(gameService.revealCell(eq(111), eq(1234)))
                .thenReturn(game);

        MvcResult result = mockMvc.perform(patch("/v1/games/111/cells/1234/reveal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.id").value(111))
                .andExpect(jsonPath("$.game.status").value(PLAYING.name()))
                .andReturn();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Flag a cell successfully while playing the game")
    void testFlagCellSuccessfully() throws Exception {
        Mockito.when(gameService.flagCell(eq(111), eq(1234)))
                .thenReturn(true);

        MvcResult result = mockMvc.perform(patch("/v1/games/111/cells/1234/flag"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
        assertThat(result).isNotNull();
    }

}
