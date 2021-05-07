package com.deviget.minesweeperapi.exceptions;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(int gameId) {
        super(String.format("Game with id %s does not have any board associated", gameId));
    }
}
