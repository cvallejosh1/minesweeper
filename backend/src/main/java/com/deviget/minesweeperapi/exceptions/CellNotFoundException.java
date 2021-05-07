package com.deviget.minesweeperapi.exceptions;

public class CellNotFoundException extends RuntimeException {

    public CellNotFoundException(int gameId, int cellId) {
        super(String.format("Cell with id %s and gameId %s not found.", cellId, gameId));
    }
}
