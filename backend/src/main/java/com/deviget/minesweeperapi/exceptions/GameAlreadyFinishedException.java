package com.deviget.minesweeperapi.exceptions;

public class GameAlreadyFinishedException extends RuntimeException {

    public GameAlreadyFinishedException(int gameId) {
        super(String.format("Game with id %s is already finished", gameId));
    }
}
