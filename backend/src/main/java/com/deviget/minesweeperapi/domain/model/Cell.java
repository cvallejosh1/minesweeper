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
public class Cell {

    private int row;
    private int col;
    private boolean mined;
    private int minesAround;
    private boolean revealed;
    private boolean flagged;


}
