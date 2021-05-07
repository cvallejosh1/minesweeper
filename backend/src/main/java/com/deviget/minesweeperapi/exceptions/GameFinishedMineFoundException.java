package com.deviget.minesweeperapi.exceptions;

public class GameFinishedMineFoundException extends RuntimeException {

    public GameFinishedMineFoundException() {
        super("Game finished because a mine was found");
    }
}
