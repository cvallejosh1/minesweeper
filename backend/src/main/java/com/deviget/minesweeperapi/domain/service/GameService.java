package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Game;

public interface GameService {

    Game createGame(int cols, int rows, int mines);
}
