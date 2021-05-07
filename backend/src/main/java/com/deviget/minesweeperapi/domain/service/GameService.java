package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Game;

import java.util.List;

public interface GameService {

    Game createGame(String userId, int cols, int rows, int mines);
    List<Game> getGamesByUser(String userId);
    Game getGameById(int gameId);
    boolean pauseGame(int gameId);
    boolean playGame(int gameId);
    Game revealCell(int gameId, int cellId);
    boolean flagCell(int gameId, int cellId);

}
