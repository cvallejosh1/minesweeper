package com.deviget.minesweeperapi.controller.api;

import com.deviget.minesweeperapi.controller.request.GameRequest;
import com.deviget.minesweeperapi.controller.response.GameListResponse;
import com.deviget.minesweeperapi.controller.response.GameResponse;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.domain.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/v1/games")
    public ResponseEntity<GameResponse> createGame(@RequestBody final GameRequest gameRequest) {
       Game game = this.gameService.createGame(gameRequest.getUserId(), gameRequest.getCols(),
               gameRequest.getRows(), gameRequest.getMines());
       return new ResponseEntity<>(GameResponse.builder().game(game).build(), HttpStatus.CREATED);
    }

    @GetMapping("/v1/users/{userId}/games")
    public ResponseEntity<?> getGamesByUser(@PathVariable final String userId) {
        List<Game> games = this.gameService.getGamesByUser(userId);
        return new ResponseEntity<>(GameListResponse.builder().games(games).build(), HttpStatus.OK);
    }

    @GetMapping("/v1/games/{gameId}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable final int gameId) {
        Game game = this.gameService.getGameById(gameId);
        return new ResponseEntity<>(GameResponse.builder().game(game).build(), HttpStatus.OK);
    }

    @PatchMapping("/v1/games/{gameId}/pause")
    public ResponseEntity<Boolean> pauseGame(@PathVariable final int gameId) {
        return new ResponseEntity<>(this.gameService.pauseGame(gameId), HttpStatus.OK);
    }

    @PatchMapping("/v1/games/{gameId}/play")
    public ResponseEntity<Boolean> playGame(@PathVariable final int gameId) {
        return new ResponseEntity<>(this.gameService.playGame(gameId), HttpStatus.OK);
    }

    @PatchMapping("/v1/games/{gameId}/cells/{cellId}/reveal")
    public ResponseEntity<GameResponse> revealCell(@PathVariable final int gameId, @PathVariable final int cellId) {
        Game game = this.gameService.revealCell(gameId, cellId);
        return new ResponseEntity<>(GameResponse.builder().game(game).build(), HttpStatus.OK);
    }

    @PatchMapping("/v1/games/{gameId}/cells/{cellId}/flag")
    public ResponseEntity<Boolean> flagCell(@PathVariable final int gameId, @PathVariable final int cellId) {
        return new ResponseEntity<>(this.gameService.flagCell(gameId, cellId), HttpStatus.OK);
    }
}
