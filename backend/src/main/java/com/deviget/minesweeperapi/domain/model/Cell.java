package com.deviget.minesweeperapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain class to represent cell object
 *
 * @author carlos.vallejos
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {

    private int x;
    private int y;
    private boolean mined;
    private int minesAround;
    private boolean revealed;
    private boolean flagged;

    public boolean isAroundTo(Cell cell) {
        return Math.abs(this.getX() - cell.getX()) <= 1 &&
                        Math.abs(this.getY() - cell.getY()) <= 1 &&
                        !(this.getX() == cell.getX() && this.getY() == cell.getY());
    }
}
