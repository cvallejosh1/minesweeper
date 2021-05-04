package com.deviget.minesweeperapi.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Domain class to represent cell object
 *
 * @author carlos.vallejos
 */
@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Board {

    int rows;
    private int cols;
    private int mines;
    private Cell[][] cells;
}
