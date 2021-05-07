package com.deviget.minesweeperapi.exceptions;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(int gameId) {
        super(String.format("Game with id %s not found", gameId));
    }
}
