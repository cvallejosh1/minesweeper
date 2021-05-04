package com.deviget.minesweeperapi.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Domain class to represent game object
 *
 * @author carlos.vallejos
 */
@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Game {

    private int id;
    private Board board;
    private GameStatus status;
}
