package com.deviget.minesweeperapi.controller.api;

import com.deviget.minesweeperapi.controller.request.GameRequest;
import com.deviget.minesweeperapi.controller.response.GameResponse;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.domain.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody final GameRequest gameRequest) {
       Game game = this.gameService.createGame(gameRequest.getCols(),
               gameRequest.getRows(), gameRequest.getMines());
       return new ResponseEntity<>(GameResponse.build(game), HttpStatus.CREATED);
    }
}
