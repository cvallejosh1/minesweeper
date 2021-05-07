package com.deviget.minesweeperapi.exceptions;

public class GameNotActiveException extends RuntimeException {

    public GameNotActiveException(int gameId) {
        super(String.format("Game with id %s is not active.", gameId));
    }
}
