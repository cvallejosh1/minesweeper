package com.deviget.minesweeperapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain class to represent game object
 *
 * @author carlos.vallejos
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private int id;
    private Board board;
    private GameStatus status;
}
